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
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;

public class WindFormat {

    Context context;
    ResourceIdFactory ids;

    public WindFormat(Context context) {
        this.context = context;
        this.ids = ResourceIdFactory.getInstance(context);
    }

    public String format(Wind wind) {
        if (wind == null) {
            return "";
        }
        if (wind.getSpeed() == Wind.UNKNOWN) {
            return "";
        }
        String format = getString("wind_caption");
        return String.format(format, valueOf(wind.getDirection()), wind.getSpeed(), valueOf(wind.getSpeedUnit()));
    }
    
    String getString(String resource) {
        return this.context.getString(this.ids.id(ResourceIdFactory.STRING, resource));
    }
	
	String valueOf(WindSpeedUnit unit){
		switch (unit){
		case MPH: return getString("wind_speed_unit_mph");
		case KMPH: return getString("wind_speed_unit_kmph");
		case MPS: return getString("wind_speed_unit_mps");
		}
		return "";
	}
	
	String valueOf(WindDirection dir){
		switch (dir){
		case E: return getString("wind_dir_e");
		case ENE: return getString("wind_dir_ene");
		case ESE: return getString("wind_dir_ese");
		case N: return getString("wind_dir_n");
		case NE: return getString("wind_dir_ne");
		case NNE: return getString("wind_dir_nne");
		case NNW: return getString("wind_dir_nnw");
		case NW: return getString("wind_dir_nw");
		case S: return getString("wind_dir_s");
		case SE: return getString("wind_dir_se");
		case SSE: return getString("wind_dir_sse");
		case SSW: return getString("wind_dir_ssw");
		case SW: return getString("wind_dir_sw");
		case W: return getString("wind_dir_w");
		case WNW: return getString("wind_dir_wnw");
		case WSW: return getString("wind_dir_wsw");
		}
		return "";
	}
	
}
