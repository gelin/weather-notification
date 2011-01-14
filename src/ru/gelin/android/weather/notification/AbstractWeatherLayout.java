package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM;

import java.util.Calendar;
import java.util.Date;

import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
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
        UnitSystem unit = UnitSystem.valueOf(preferences.getString(UNIT_SYSTEM, "SI"));
        
        Temperature temp = currentCondition.getTemperature(unit);
        setVisibility(R.id.temp, View.VISIBLE);
        setText(R.id.current_temp, formatTemp(temp.getCurrent()));
        setText(R.id.high_temp, formatTemp(temp.getHigh()));
        setText(R.id.low_temp, formatTemp(temp.getLow()));
        
        setVisibility(R.id.forecasts, View.VISIBLE);
        bindForecast(weather, unit, 1,
                R.id.forecast_1, R.id.forecast_day_1,
                R.id.forecast_condition_1,
                R.id.forecast_high_temp_1, R.id.forecast_low_temp_1);
        bindForecast(weather, unit, 2,
                R.id.forecast_2, R.id.forecast_day_2,
                R.id.forecast_condition_2,
                R.id.forecast_high_temp_2, R.id.forecast_low_temp_2);
        bindForecast(weather, unit, 3,
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
    
    abstract void setText(int viewId, String text);
    
    abstract void setVisibility(int viewId, int visibility);
    
    static String formatTemp(int temp) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        if (temp > 0) {
            return "+" + String.valueOf(temp) + "\u00B0";
        }
        if (temp < 0) {
            return String.valueOf(temp) + "\u00B0";
        }
        return "0\u00B0";
    }
    
    Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

}
