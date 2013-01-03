/*
 *  Weather API.
 *  Copyright (C) 2011  Vladimir Kubyshev, Denis Nelubin
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
 */

package ru.gelin.android.weather.google;

import ru.gelin.android.weather.SimpleHumidity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Humidity which parses the string provided by Google Weather API.
 */
public class GoogleHumidity extends SimpleHumidity {
    
    private static final Pattern PARSE_PATTERN = Pattern.compile("(\\d++)");

    /**
     *  Sets the current humidity from the unparsed text.
     */
    public void parseText(String text) {
        this.text = text;
        if (text == null || text.length() == 0) {
            this.value = UNKNOWN;
            return;
        }
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.value  = Integer.parseInt(matcher.group(1));   //this group selects integers, so parsing is safe
        }
    }

}
