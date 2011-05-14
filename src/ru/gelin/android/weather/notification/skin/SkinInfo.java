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
import android.content.Context;
import android.preference.CheckBoxPreference;

/**
 * 	Information about skin
 */
public class SkinInfo {

	String packageName;
	boolean enabled;
	String broadcastReceiverClass;
	String broadcastReceiverLabel;
	String configActivityClass;
	String configActivityLabel;
	
	public String getPackageName() {
		return packageName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getBroadcastReceiverClass() {
		return broadcastReceiverClass;
	}
	
	public String getBroadcastReceiverLabel() {
		return broadcastReceiverLabel;
	}

	public String getConfigActivityClass() {
		return configActivityClass;
	}
	
	public String getConfigActivityLabel() {
		return configActivityLabel;
	}
	
	/**
	 *  Creates checkbox preference to enable/disable the activity.
	 */
	CheckBoxPreference getCheckBoxPreference(Context context) {
	    CheckBoxPreference checkBox = new CheckBoxPreference(context);
	    checkBox.setKey(String.format(SKIN_ENABLED_PATTERN, getPackageName()));
        checkBox.setTitle(getBroadcastReceiverLabel());
        return checkBox;
	}
	
}
