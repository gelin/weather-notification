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

package ru.gelin.android.weather.google;

import ru.gelin.android.weather.SimpleWind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Wind speed and direction which can parse the text string returned by Goolge Weather API.
 */
public class GoogleWind extends SimpleWind {

    private static final Pattern PARSE_PATTERN = Pattern.compile("(N|NNE|NE|ENE|E|ESE|SE|SSE|S|SSW|SW|WSW|W|WNW|NW|NNW)\\s+at\\s+(\\d+)");

    /**
     *  Constructs the wind.
     *  The MPH is implied as speed unit.
     */
    public GoogleWind() {
        super(WindSpeedUnit.MPH);
    }

    /**
     *  Extract wind speed and direction value from string.
     */
    public void parseText(String text) {
        this.text = text;
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.direction  = WindDirection.valueOf(matcher.group(1));  //regexp gurantees that the value is valid enum value
            this.speed  = Integer.parseInt(matcher.group(2));   //regexp guarantees that the value is valid integer
        }
    }

}
