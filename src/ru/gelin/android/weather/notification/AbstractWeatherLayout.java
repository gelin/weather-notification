/*
 *  Android Weather Notification.
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

package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.skin.builtin.PreferenceKeys.TEMP_UNIT;
import static ru.gelin.android.weather.notification.skin.builtin.PreferenceKeys.TEMP_UNIT_DEFAULT;

import java.util.Calendar;
import java.util.Date;

import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.skin.builtin.TemperatureUnit;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

/**
 *  Utility to layout weather values.
 */
public abstract class AbstractWeatherLayout {
    
    /** Current context */
    protected Context context;
    
    /**
     *  Creates the utility for specified context.
     */
    protected AbstractWeatherLayout(Context context) {
        this.context = context;
    }
    
    /**
     *  Lays out the weather values on a view.
     */
    public void bind(Weather weather) {
        if (weather.isEmpty()) {
            emptyViews();
        } else {
            bindViews(weather);
        }

    }
    
    void bindViews(Weather weather) {
        Date timeValue = weather.getTime();
        if (timeValue.getTime() == 0) {
            setText(R.id.update_time, "");
        } else if (isDate(timeValue)) {
            setText(R.id.update_time, this.context.getString(
                    R.string.update_date_format, timeValue));
        } else {
            setText(R.id.update_time, this.context.getString(
                R.string.update_time_format, timeValue));
        }
        setText(R.id.location, weather.getLocation().getText());
        
        if (weather.getConditions().size() <= 0) {
            return;
        }
        WeatherCondition currentCondition = weather.getConditions().get(0);
        setText(R.id.condition, currentCondition.getConditionText());
        setText(R.id.humidity, currentCondition.getHumidityText());
        setText(R.id.wind, currentCondition.getWindText());
        
        SharedPreferences preferences = 
                PreferenceManager.getDefaultSharedPreferences(this.context);
        TemperatureUnit unit = TemperatureUnit.valueOf(preferences.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        
        Temperature tempC = currentCondition.getTemperature(UnitSystem.SI);
        Temperature tempF = currentCondition.getTemperature(UnitSystem.US);
        UnitSystem mainUnit = unit.getUnitSystem();
        Temperature mainTemp = currentCondition.getTemperature(mainUnit);
        
        setVisibility(R.id.temp, View.VISIBLE);
        setText(R.id.current_temp, formatTemp(mainTemp.getCurrent(), unit));
        switch(unit) {
        case C: case F:
            setVisibility(R.id.current_temp_alt, View.GONE);
            break;
        case CF:
            setText(R.id.current_temp_alt, formatTemp(tempF.getCurrent(),
                    TemperatureUnit.F));
            setVisibility(R.id.current_temp_alt, View.VISIBLE);
            break;
        case FC:
            setText(R.id.current_temp_alt, formatTemp(tempC.getCurrent(),
                    TemperatureUnit.C));
            setVisibility(R.id.current_temp_alt, View.VISIBLE);
            break;
        }
        setText(R.id.high_temp, formatTemp(mainTemp.getHigh()));
        setText(R.id.low_temp, formatTemp(mainTemp.getLow()));
        
        setVisibility(R.id.forecasts, View.VISIBLE);
        bindForecast(weather, mainUnit, 1,
                R.id.forecast_1, R.id.forecast_day_1,
                R.id.forecast_condition_1,
                R.id.forecast_high_temp_1, R.id.forecast_low_temp_1);
        bindForecast(weather, mainUnit, 2,
                R.id.forecast_2, R.id.forecast_day_2,
                R.id.forecast_condition_2,
                R.id.forecast_high_temp_2, R.id.forecast_low_temp_2);
        bindForecast(weather, mainUnit, 3,
                R.id.forecast_3, R.id.forecast_day_3,
                R.id.forecast_condition_3,
                R.id.forecast_high_temp_3, R.id.forecast_low_temp_3);
        
    }
    
    void bindForecast(Weather weather, 
            UnitSystem unit, int i, 
            int groupId, int dayId, int conditionId, 
            int highTempId, int lowTempId) {
        if (weather.getConditions().size() > i) {
            setVisibility(groupId, View.VISIBLE);
            Date tomorrow = addDays(weather.getTime(), i);
            setText(dayId, context.getString(R.string.forecast_day_format, tomorrow));
            WeatherCondition forecastCondition = weather.getConditions().get(i);
            setText(conditionId, forecastCondition.getConditionText());
            Temperature forecastTemp = forecastCondition.getTemperature(unit);
            setText(highTempId, formatTemp(forecastTemp.getHigh()));
            setText(lowTempId, formatTemp(forecastTemp.getLow()));
        } else {
            setVisibility(groupId, View.GONE);
        }
    }
    
    void emptyViews() {
        setText(R.id.update_time, "");
        setText(R.id.location, "");
        setText(R.id.condition, context.getString(R.string.unknown_weather));
        setText(R.id.humidity, "");
        setText(R.id.wind, "");
        
        setVisibility(R.id.temp, View.INVISIBLE);
        
        setVisibility(R.id.forecasts, View.GONE);
    }
    
    protected abstract void setText(int viewId, String text);
    
    protected abstract void setVisibility(int viewId, int visibility);
    
    public static String formatTemp(int temp) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        return temp + "\u00B0";
    }
    
    public static String formatTemp(int temp, TemperatureUnit unit) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        switch (unit) {
        case C:
            return temp + "\u00B0C";
        case F:
            return temp + "\u00B0F";
        case CF:
            return temp + "\u00B0C";
        case FC:
            return temp + "\u00B0F";
        }
        return "";
    }
    
    public static String formatTemp(int tempC, int tempF, TemperatureUnit unit) {
        if (tempC == Temperature.UNKNOWN || tempF == Temperature.UNKNOWN) {
            return "";
        }
        switch (unit) {
        case C:
            return tempC + "\u00B0C";
        case F:
            return tempF + "\u00B0F";
        case CF:
            return tempC + "\u00B0C(" + tempF + "\u00B0F)";
        case FC:
            return tempF + "\u00B0F(" + tempC + "\u00B0C)";
        }
        return "";
    }
    
    Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
    
    /**
     *  Returns true if the provided date has zero (0:00:00) time.
     */
    boolean isDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR) == 0 &&
                calendar.get(Calendar.MINUTE) == 0 &&
                calendar.get(Calendar.SECOND) == 0 &&
                calendar.get(Calendar.MILLISECOND) == 0;
    }

}
