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

import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;
import ru.gelin.android.weather.notification.skin.R;
import android.content.Context;

public class WindFormatter {
	Context context;
	
	String SpeedToStr(WindSpeedUnit unit){
		switch (unit){
		case MPH: return context.getString(R.string.wind_speed_unit_mph);
		case KMPH: return context.getString(R.string.wind_speed_unit_kmph);
		case MPS: return context.getString(R.string.wind_speed_unit_mps);
		}
		return "";
	}
	
	String DirToStr(WindDirection dir){
		switch (dir){
		case E: return context.getString(R.string.wind_dir_e);
		case ENE: return context.getString(R.string.wind_dir_ene);
		case ESE: return context.getString(R.string.wind_dir_ese);
		case N: return context.getString(R.string.wind_dir_n);
		case NE: return context.getString(R.string.wind_dir_ne);
		case NNE: return context.getString(R.string.wind_dir_nne);
		case NNW: return context.getString(R.string.wind_dir_nnw);
		case NW: return context.getString(R.string.wind_dir_nw);
		case S: return context.getString(R.string.wind_dir_s);
		case SE: return context.getString(R.string.wind_dir_se);
		case SSE: return context.getString(R.string.wind_dir_sse);
		case SSW: return context.getString(R.string.wind_dir_ssw);
		case SW: return context.getString(R.string.wind_dir_sw);
		case W: return context.getString(R.string.wind_dir_w);
		case WNW: return context.getString(R.string.wind_dir_wnw);
		case WSW: return context.getString(R.string.wind_dir_wsw);
		}
		return "";
	}
	
    public String format(int speed, WindDirection dir, WindSpeedUnit unit, Context context) {
    	this.context = context;
        if (speed == Wind.UNKNOWN) {
            return "";
        }
        String fmt = context.getString(R.string.wind_caption);
        return String.format(fmt, DirToStr(dir), speed, SpeedToStr(unit));
    }
}
