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

import static ru.gelin.android.weather.notification.Tag.TAG;
import static ru.gelin.android.weather.notification.skin.WeatherNotificationReceiver.ACTION_WEATHER_UPDATE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

/**
 * 	Contains methods to handle list of installed skins.
 */
public class SkinManager {
	
	Context context;
	/** Map of the skin package name to the skin info. Sorted by package name. */
	Map<String, SkinInfo> skins = new TreeMap<String, SkinInfo>(); //sorted by the package name
	
	public SkinManager(Context context) {
		this.context = context;
	}

	/**
	 *  Rereads the list of installed skins.
	 */
	public List<SkinInfo> getInstalledSkins() {
	    skins.clear();
		querySkinReceivers();
		
		List<SkinInfo> result = new ArrayList<SkinInfo>();
		result.addAll(skins.values());
		return result;
	}
	
	public List<SkinInfo> getEnabledSkins() {
		//TODO
		return null;
	}
	
	public void setSkinEnabled(SkinInfo skin, boolean enabled) {
		//TODO
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
            Log.d(TAG, "class: " + packageName);
            SkinInfo skin = new SkinInfo();
            skin.packageName = packageName;
            skin.broadcastReceiverLabel = label;
            skin.broadcastReceiverClass = receiverClass;
            
            skins.put(packageName, skin);
        }
	}
	
}
