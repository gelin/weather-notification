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

/**
 * 	Information about skin
 */
public class SkinInfo {

	String packageName;
	boolean enabled;
	String broadcastReceiverClass;
	String broadcaseReceiverLabel;
	String configActivityClass;
	String configActivityLabel;
	
	SkinInfo(String packageName, boolean enabled,
			String broadcastReceiverClass, String broadcaseReceiverLabel,
			String configActivityClass, String configActivityLabel) {
		this.packageName = packageName;
		this.enabled = enabled;
		this.broadcastReceiverClass = broadcastReceiverClass;
		this.broadcaseReceiverLabel = broadcaseReceiverLabel;
		this.configActivityClass = configActivityClass;
		this.configActivityLabel = configActivityLabel;
	}

	public String getPackageName() {
		return packageName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getBroadcastReceiverClass() {
		return broadcastReceiverClass;
	}
	
	public String getBroadcaseReceiverLabel() {
		return broadcaseReceiverLabel;
	}

	public String getConfigActivityClass() {
		return configActivityClass;
	}
	
	public String getConfigActivityLabel() {
		return configActivityLabel;
	}
	
}
