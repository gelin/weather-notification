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

package ru.gelin.android.weather.source;

import android.test.AndroidTestCase;

public class HttpUtilsTest extends AndroidTestCase {

    public void testGetCharset() {
        assertEquals("UTF-8", HttpUtils.getCharset("text/xml; charset=UTF-8"));
        assertEquals("windows-1251", HttpUtils.getCharset("text/xml; charset=windows-1251"));
        assertEquals("UTF-8", HttpUtils.getCharset("text/xml"));
        assertEquals("windows-1251", HttpUtils.getCharset("text/xml; charset=windows-1251; something more"));
        assertEquals("UTF-8", HttpUtils.getCharset(""));
        assertEquals("UTF-8", HttpUtils.getCharset((String) null));
    }

}
