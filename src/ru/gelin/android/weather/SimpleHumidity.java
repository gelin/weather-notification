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

package ru.gelin.android.weather;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vladimir
 *
 */
public class SimpleHumidity implements Humidity {

    private static final Pattern PARSE_PATTERN = Pattern.compile("(\\d++)");

    int value = UNKNOWN;
    String text = "";

    /**
     *  Sets the current humidity.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     *  Sets the current humidity.
     */
    public void setText(String text) {
        if (text.length() > 0) {
            this.text = text;
        }
    }

    /**
     *  Sets the current humidity.
     */
    public void setTextParse(String text) {
        if (text.length() > 0) {
            parseText(text);
        }
    }

    //@Override
    public int getValue() {
        if (this.value == UNKNOWN) {
            return 0; 
        }
        return this.value;
    }

    //@Override
    public String getText() {
        return this.text;
    }

    /**
     * Extract from string humidity value
     */
    void parseText(String text) {
        //TODO: add tests
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.value  = Integer.parseInt(matcher.group(1));   //TODO: catch when non-integer
        }
    }

}
