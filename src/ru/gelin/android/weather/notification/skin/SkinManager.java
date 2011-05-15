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

import static ru.gelin.android.weather.notification.skin.PreferenceKeys.SKIN_ENABLED_PATTERN;
import static ru.gelin.android.weather.notification.skin.WeatherNotificationReceiver.ACTION_WEATHER_UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.gelin.android.weather.notification.Tag;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;

/**
 * 	Contains methods to handle list of installed skins.
 */
public class SkinManager {
	
    /** Intent action for the skin configuration activity */
    public static final String ACTION_WEATHER_SKIN_CONFIG =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_SKIN_CONFIG";
    
    
	Context context;
	/** Map of the skin package name to the skin info. Sorted by package name. */
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
	 *  Queries PackageManager for broadcast receivers which handles ACTION_WEATHER_UPDATE.
	 *  Puts found data (skin package, receiver class and labed) into skins map. 
	 */
	void querySkinReceivers() {
	    PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(ACTION_WEATHER_UPDATE);
        List<ResolveInfo> search = pm.queryBroadcastReceivers(intent, 0);   //without flags
        
        for (ResolveInfo info : search) {
            //Log.d(TAG, String.valueOf(info));
            String packageName = info.activityInfo.packageName;
            String label = String.valueOf(info.loadLabel(pm));
            String receiverClass = info.activityInfo.name;
            //Log.d(TAG, "package: " + packageName);
            //Log.d(TAG, "class: " + receiverClass);
            SkinInfo skin = new SkinInfo();
            skin.packageName = packageName;
            skin.broadcastReceiverLabel = label;
            skin.broadcastReceiverClass = receiverClass;
            
            this.skins.put(packageName, skin);
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
	    return packageName.equals(this.context.getPackageName());
	}
	
	/**
     *  Queries PackageManager for activities which handles ACTION_WEATHER_SKIN_CONFIG actions.
     *  Updates the skin map according the package name. 
     */
    void querySkinConfigs() {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(ACTION_WEATHER_SKIN_CONFIG);
        List<ResolveInfo> search = pm.queryIntentActivities(intent, 0);   //without flags
        
        for (ResolveInfo info : search) {
            //Log.d(TAG, String.valueOf(info));
            String packageName = info.activityInfo.packageName;
            String label = String.valueOf(info.loadLabel(pm));
            String activityClass = info.activityInfo.name;
            //Log.d(TAG, "package: " + packageName);
            //Log.d(TAG, "class: " + activityClass);
            SkinInfo skin = this.skins.get(packageName);
            if (skin == null) {
                continue;
            }
            skin.packageName = packageName;
            skin.configActivityLabel = label;
            skin.configActivityClass = activityClass;
        }
    }
	
}
