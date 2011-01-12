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
import android.widget.TextView;

/**
 *  Utility to layout weather values.
 */
public class WeatherLayout {
    
    /** Current context */
    Context context;
    
    /**
     *  Creates the utility for specified context.
     */
    public WeatherLayout(Context context) {
        this.context = context;
    }
    
    /**
     *  Lays out the weather values on specified view.
     */
    public void bind(Weather weather, View view) {
        if (weather.isEmpty()) {
            emptyViews(view);
        } else {
            bindViews(weather, view);
        }

    }
    
    void bindViews(Weather weather, View view) {
        TextView time = (TextView)view.findViewById(R.id.update_time);
        Date timeValue = weather.getTime();
        if (timeValue.getTime() == 0) {
            setText(time, "");
        } else {
            setText(time, this.context.getString(
                R.string.update_time_format, timeValue));
        }
        TextView location = (TextView)view.findViewById(R.id.location);
        setText(location, weather.getLocation().getText());
        
        if (weather.getConditions().size() <= 0) {
            return;
        }
        WeatherCondition currentCondition = weather.getConditions().get(0);
        TextView condition = (TextView)view.findViewById(R.id.condition);
        setText(condition, currentCondition.getConditionText());
        TextView humidity = (TextView)view.findViewById(R.id.humidity);
        setText(humidity, currentCondition.getHumidityText());
        TextView wind = (TextView)view.findViewById(R.id.wind);
        setText(wind, currentCondition.getWindText());
        
        SharedPreferences preferences = 
                PreferenceManager.getDefaultSharedPreferences(this.context);
        UnitSystem unit = UnitSystem.valueOf(preferences.getString(UNIT_SYSTEM, "SI"));
        
        Temperature temp = currentCondition.getTemperature(unit);
        View tempSection = view.findViewById(R.id.temp);
        setVisibility(tempSection, View.VISIBLE);
        TextView currentTemp = (TextView)view.findViewById(R.id.current_temp);
        setText(currentTemp, formatTemp(temp.getCurrent()));
        TextView highTemp = (TextView)view.findViewById(R.id.high_temp);
        setText(highTemp, formatTemp(temp.getHigh()));
        TextView lowTemp = (TextView)view.findViewById(R.id.low_temp);
        setText(lowTemp, formatTemp(temp.getLow()));
        
        View forecasts = view.findViewById(R.id.forecasts);
        setVisibility(forecasts, View.VISIBLE);
        bindForecast(weather, view, unit, 1,
                R.id.forecast_1, R.id.forecast_day_1,
                R.id.forecast_condition_1,
                R.id.forecast_high_temp_1, R.id.forecast_low_temp_1);
        bindForecast(weather, view, unit, 2,
                R.id.forecast_2, R.id.forecast_day_2,
                R.id.forecast_condition_2,
                R.id.forecast_high_temp_2, R.id.forecast_low_temp_2);
        bindForecast(weather, view, unit, 3,
                R.id.forecast_3, R.id.forecast_day_3,
                R.id.forecast_condition_3,
                R.id.forecast_high_temp_3, R.id.forecast_low_temp_3);
        
    }
    
    void bindForecast(Weather weather, View view, 
            UnitSystem unit, int i, 
            int groupId, int dayId, int conditionId, 
            int highTempId, int lowTempId) {
        if (weather.getConditions().size() > i) {
            View forecast1 = view.findViewById(groupId);
            setVisibility(forecast1, View.VISIBLE);
            Date tomorrow = addDays(weather.getTime(), i);
            TextView forecastDay = (TextView)view.findViewById(dayId);
            setText(forecastDay, context.getString(R.string.forecast_day_format, tomorrow));
            WeatherCondition forecastCondition = weather.getConditions().get(i);
            TextView forecastConditionText = (TextView)view.findViewById(conditionId);
            setText(forecastConditionText, forecastCondition.getConditionText());
            Temperature forecastTemp = forecastCondition.getTemperature(unit);
            TextView forecastHighTemp = (TextView)view.findViewById(highTempId);
            setText(forecastHighTemp, formatTemp(forecastTemp.getHigh()));
            TextView forecastLowTemp = (TextView)view.findViewById(lowTempId);
            setText(forecastLowTemp, formatTemp(forecastTemp.getLow()));
        } else {
            View forecast1 = view.findViewById(R.id.forecast_1);
            setVisibility(forecast1, View.GONE);
        }
    }
    
    void emptyViews(View view) {
        TextView time = (TextView)view.findViewById(R.id.update_time);
        setText(time, "");
        TextView location = (TextView)view.findViewById(R.id.location);
        setText(location, "");
        TextView condition = (TextView)view.findViewById(R.id.condition);
        setText(condition, context.getString(R.string.unknown_weather));
        TextView humidity = (TextView)view.findViewById(R.id.humidity);
        setText(humidity, "");
        TextView wind = (TextView)view.findViewById(R.id.wind);
        setText(wind, "");
        
        View tempSection = view.findViewById(R.id.temp);
        setVisibility(tempSection, View.INVISIBLE);
        
        View forecasts = view.findViewById(R.id.forecasts);
        setVisibility(forecasts, View.GONE);
    }
    
    void setText(TextView view, String text) {
        if (view == null) {
            return;
        }
        if (text == null) {
            view.setText("");
            return;
        }
        view.setText(text);
    }
    
    void setVisibility(View view, int visibility) {
        if (view == null) {
            return;
        }
        view.setVisibility(visibility);
    }
    
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
