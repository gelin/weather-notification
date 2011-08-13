package ru.gelin.android.weather.notification.skin.impl;

import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;
import ru.gelin.android.weather.notification.R;
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
