/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import ru.gelin.android.weather.notification.IntentParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ru.gelin.android.weather.notification.IntentParameters.ACTION_WEATHER_UPDATE;
import static ru.gelin.android.weather.notification.IntentParameters.ACTION_WEATHER_UPDATE_2;
import static ru.gelin.android.weather.notification.skin.PreferenceKeys.SKIN_ENABLED_PATTERN;

/**
 * 	Contains methods to handle list of installed skins.
 * 	The BroadcastReceivers which handles ACTION_WEATHER_UPDATE and ACTION_WEATHER_UPDATE_2
 * 	intents are tracked.
 * 	The activity which handles ACTION_WEATHER_SKIN_PREFERENCES intent and is placed in the same
 * 	Android AND Java package as the received is treated as a configuration activity for the skin.
 */
public class SkinManager {
	
    Context context;
	/** Map of the skin Android package name + Java package name to the skin info. Sorted by package name. */
	Map<String, SkinInfo> skins = new TreeMap<String, SkinInfo>(); //sorted by the package name
	
	/**
	 *  Creates the manager. Updates the list of installed skins.
	 */
	public SkinManager(Context context) {
		this.context = context;
		querySkinReceivers();
		updateEnabledFlag();
		querySkinConfigs();
	}

	/**
	 *  Returns the list of installed skins.
	 */
	public List<SkinInfo> getInstalledSkins() {
		List<SkinInfo> result = new ArrayList<SkinInfo>();
		result.addAll(this.skins.values());
		return result;
	}
	
	/**
	 *  Returns the list of enabled skins.
	 */
	public List<SkinInfo> getEnabledSkins() {
	    List<SkinInfo> result = new ArrayList<SkinInfo>();
	    for (SkinInfo skin : this.skins.values()) {
	        if (skin.isEnabled()) {
	            result.add(skin);
	        }
	    }
        return result;
	}
	
	/**
	 *  Returns the list of installed, but disabled skins.
	 */
	public List<SkinInfo> getDisabledSkins() {
        List<SkinInfo> result = new ArrayList<SkinInfo>();
        for (SkinInfo skin : this.skins.values()) {
            if (!skin.isEnabled()) {
                result.add(skin);
            }
        }
        return result;
    }

    /**
     *  Queries PackageManager for broadcast receivers which handles
     *  ACTION_WEATHER_UPDATE and ACTION_WEATHER_UPDATE_2.
     *  Puts found data (skin package, receiver class and label) into skins map.
     */
    void querySkinReceivers() {
        querySkinReceivers(ACTION_WEATHER_UPDATE, SkinInfo.Version.V1);     //TODO: join intent action names and versions...
        querySkinReceivers(ACTION_WEATHER_UPDATE_2, SkinInfo.Version.V2);
    }

	/**
	 *  Queries PackageManager for broadcast receivers which handles specified Action.
	 *  Puts found data (skin package, receiver class and labed) into skins map. 
	 */
	void querySkinReceivers(String action, SkinInfo.Version version) {
	    PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(action);
        List<ResolveInfo> search = pm.queryBroadcastReceivers(intent, 0);   //without flags

        for (ResolveInfo info : search) {
            //Log.d(TAG, String.valueOf(info));
            String packageName = info.activityInfo.packageName;
            String label = String.valueOf(info.loadLabel(pm));
            String receiverClass = info.activityInfo.name;
            //Log.d(TAG, "package: " + packageName);
            //Log.d(TAG, "class: " + receiverClass);
            SkinInfo skin = SkinInfo.getInstance(getSkinId(info));
            skin.packageName = packageName;
            skin.version = version;
            skin.broadcastReceiverLabel = label;
            skin.broadcastReceiverClass = receiverClass;
            this.skins.put(skin.getId(), skin);
        }

        int order = 0;
        for (SkinInfo skin : this.skins.values()) {
            skin.order = order;
            order += 2;
        }
	}

	/**
	 *  Checks preferences to found which skin is enabled.
	 */
	void updateEnabledFlag() {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
	    for (Map.Entry<String, SkinInfo> skin : this.skins.entrySet()) {
	        String key = String.format(SKIN_ENABLED_PATTERN, skin.getKey());
	        boolean enabled = prefs.getBoolean(key, isBuiltinSkin(skin.getKey()));     //builtin skin is enabled by default
	        skin.getValue().enabled = enabled;
	    }
	}
	
	boolean isBuiltinSkin(String packageName) {
	    return packageName.startsWith(this.context.getPackageName()) && packageName.endsWith("builtin");
	}
	
	/**
     *  Queries PackageManager for activities which handles ACTION_WEATHER_SKIN_PREFERENCES actions.
     *  Updates the skin map according the package name. 
     */
    void querySkinConfigs() {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(IntentParameters.ACTION_WEATHER_SKIN_PREFERENCES);
        List<ResolveInfo> search = pm.queryIntentActivities(intent, 0);   //without flags
        
        for (ResolveInfo info : search) {
            //Log.d(TAG, String.valueOf(info));
            String label = String.valueOf(info.loadLabel(pm));
            String activityClass = info.activityInfo.name;
            //Log.d(TAG, "package: " + packageName);
            //Log.d(TAG, "class: " + activityClass);
            SkinInfo skin = this.skins.get(getSkinId(info));
            if (skin == null) {
                continue;
            }
            skin.configActivityLabel = label;
            skin.configActivityClass = activityClass;
        }
    }

    String getSkinId(ResolveInfo info) {
        String androidPackageName = info.activityInfo.packageName;
        String className = info.activityInfo.name;
        String javaPackageName = className.substring(0, className.lastIndexOf('.'));
        return androidPackageName + "/" + javaPackageName;
    }
	
}
