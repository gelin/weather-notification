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

import org.apache.http.HttpEntity;

/**
 *  Some utilities to work with HTTP requests.
 */
public class HttpUtils {

    private HttpUtils() {
        //avoid instatiation
    }

    public static String getCharset(HttpEntity entity) {
        return getCharset(entity.getContentType().toString());
    }

    static String getCharset(String contentType) {
        if (contentType == null) {
            return HttpWeatherSource.ENCODING;
        }
        int charsetPos = contentType.indexOf(HttpWeatherSource.CHARSET);
        if (charsetPos < 0) {
            return HttpWeatherSource.ENCODING;
        }
        charsetPos += HttpWeatherSource.CHARSET.length();
        int endPos = contentType.indexOf(';', charsetPos);
        if (endPos < 0) {
            endPos = contentType.length();
        }
        return contentType.substring(charsetPos, endPos);
    }
}
