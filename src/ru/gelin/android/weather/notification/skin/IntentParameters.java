/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin;

import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.Tag;

public class IntentParameters {

    /** Intent action which should be accepted by the receiver */ 
    public static final String ACTION_WEATHER_UPDATE =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_UPDATE";
    
    /** Intent extra which contains {@link Weather} */ 
    public static final String EXTRA_WEATHER =
        Tag.class.getPackage().getName() + ".EXTRA_WEATHER";
    
    /** Intent extra which contains boolean flag */ 
    public static final String EXTRA_ENABLE_NOTIFICATION =
        Tag.class.getPackage().getName() + ".EXTRA_ENABLE_NOTIFICATION";
    
    /** Intent action for the skin configuration activity */
    public static final String ACTION_WEATHER_SKIN_PREFERENCES =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_SKIN_PREFERENCES";

}
