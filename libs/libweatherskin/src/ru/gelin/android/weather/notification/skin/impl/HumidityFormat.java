/*
 *  Android Weather Notification.
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

package ru.gelin.android.weather.notification.skin.impl;

import android.content.Context;
import ru.gelin.android.weather.Humidity;

import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;

public class HumidityFormat {

    Context context;
    ResourceIdFactory ids;

    public HumidityFormat(Context context) {
        this.context = context;
        this.ids = ResourceIdFactory.getInstance(context);
    }
	
    public String format(Humidity humidity) {
        if (humidity == null) {
            return "";
        }
        if (humidity.getValue() == Humidity.UNKNOWN) {
            return "";
        }
        return String.format(this.context.getString(
                this.ids.id(STRING, "humidity_caption")),
                humidity.getValue());
    }
}
