/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.notification;

import ru.gelin.android.weather.Weather;

public class IntentParameters {

    /**
     *  Intent action which should be accepted by the receiver.
     *  Intent with this action is sent to the old skins and is deprecated.
     *  Has "ru.gelin.android.weather.notification.ACTION_WEATHER_UPDATE" value.
     */
    @Deprecated
    public static final String ACTION_WEATHER_UPDATE =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_UPDATE";

    /**
     *  Intent action which should be accepted by the receiver.
     *  This is new intent action which should be used in the code of new skins.
     *  This action was added in release 0.3.
     *  Has "ru.gelin.android.weather.notification.ACTION_WEATHER_UPDATE_2" value.
     */
    public static final String ACTION_WEATHER_UPDATE_2 =
            Tag.class.getPackage().getName() + ".ACTION_WEATHER_UPDATE_2";
    
    /**
     *  Intent extra which contains {@link Weather}.
     *  Has "ru.gelin.android.weather.notification.EXTRA_WEATHER" value.
     *  Note that this extra may contain objects of different types, depend on the Action of the Intent.
     */
    public static final String EXTRA_WEATHER =
            Tag.class.getPackage().getName() + ".EXTRA_WEATHER";
    
    /** Intent extra which contains boolean flag */ 
    public static final String EXTRA_ENABLE_NOTIFICATION =
        Tag.class.getPackage().getName() + ".EXTRA_ENABLE_NOTIFICATION";
    
    /** Intent action for the skin configuration activity */
    public static final String ACTION_WEATHER_SKIN_PREFERENCES =
        Tag.class.getPackage().getName() + ".ACTION_WEATHER_SKIN_PREFERENCES";

    private IntentParameters() {
        //avoid instantiation
    }

}
