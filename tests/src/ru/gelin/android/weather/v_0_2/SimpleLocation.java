/*
 *  Weather API.
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

package ru.gelin.android.weather.v_0_2;

/**
 *  Simple location which query and text are equal and are set in constructor.
 */
public class SimpleLocation implements Location {

    String text;
    
    /**
     *  Create the location.
     *  @param locationText query and the text value.
     */
    public SimpleLocation(String text) {
        this.text = text;
    }
    
    //@Override
    public String getQuery() {
        return this.text;
    }

    //@Override
    public String getText() {
        return this.text;
    }
    
    //@Override
    public boolean isEmpty() {
        return this.text == null || this.text.length() == 0;
    }

}
