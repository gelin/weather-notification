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

import static ru.gelin.android.weather.notification.skin.IntentParameters.ACTION_WEATHER_SKIN_PREFERENCES;
import static ru.gelin.android.weather.notification.skin.PreferenceKeys.SKIN_CONFIG_PATTERN;
import static ru.gelin.android.weather.notification.skin.PreferenceKeys.SKIN_ENABLED_PATTERN;
import android.content.Context;
import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

/**
 * 	Information about skin
 */
public class SkinInfo {

    /**
     *  Versions of the skin.
     *  Skin V1 receives notifications with {@link IntentParameters#ACTION_WEATHER_UPDATE}.
     *  Skin V2 receives notifications with {@link IntentParameters#ACTION_WEATHER_UPDATE_2}.
     */
    public enum Version {
        V1, V2;
    }

	String packageName;
    Version version;
	boolean enabled;
	String broadcastReceiverClass;
	String broadcastReceiverLabel;
	String configActivityClass;
	String configActivityLabel;
	
	public String getPackageName() {
		return this.packageName;
	}

    public Version getVersion() {
        return this.version;
    }
	
	public boolean isEnabled() {
		return this.enabled;
	}

	public String getBroadcastReceiverClass() {
		return this.broadcastReceiverClass;
	}
	
	public String getBroadcastReceiverLabel() {
		return this.broadcastReceiverLabel;
	}

	public String getConfigActivityClass() {
		return this.configActivityClass;
	}
	
	public String getConfigActivityLabel() {
		return this.configActivityLabel;
	}
	
	/**
	 *  Creates checkbox preference to enable/disable the activity.
	 */
	CheckBoxPreference getCheckBoxPreference(Context context) {
	    CheckBoxPreference checkBox = new CheckBoxPreference(context);
	    checkBox.setKey(String.format(SKIN_ENABLED_PATTERN, getPackageName()));
        checkBox.setTitle(getBroadcastReceiverLabel());
        checkBox.setChecked(isEnabled());
        return checkBox;
	}
	
	/**
	 *  Creates preference to open skin settings.
	 *  Can return null if the skin has no configuraion.
	 */
	Preference getConfigPreference(Context context) {
	    if (getConfigActivityClass() == null) {
	        return null;
	    }
	    Preference pref = new Preference(context);
        pref.setKey(String.format(SKIN_CONFIG_PATTERN, getPackageName()));
        pref.setTitle(getConfigActivityLabel() == null ? getBroadcastReceiverLabel() : getConfigActivityLabel());
        Intent intent = new Intent(ACTION_WEATHER_SKIN_PREFERENCES);
        intent.setClassName(getPackageName(), getConfigActivityClass());
        pref.setIntent(intent);
        return pref;
	}
	
}
