package ru.gelin.android.weather.notification;

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
    
    /** Unit system preference key */
    static final String UNIT_SYSTEM = "unit_system";
    
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
        tempSection.setVisibility(View.VISIBLE);
        TextView currentTemp = (TextView)view.findViewById(R.id.current_temp);
        setText(currentTemp, formatTemp(temp.getCurrent()));
        TextView highTemp = (TextView)view.findViewById(R.id.high_temp);
        setText(highTemp, formatTemp(temp.getHigh()));
        TextView lowTemp = (TextView)view.findViewById(R.id.low_temp);
        setText(lowTemp, formatTemp(temp.getLow()));
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
    
    String formatTemp(int temp) {
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
    
    void emptyViews(View view) {
        TextView time = (TextView)view.findViewById(R.id.update_time);
        time.setText("");
        TextView location = (TextView)view.findViewById(R.id.location);
        location.setText("");
        TextView condition = (TextView)view.findViewById(R.id.condition);
        condition.setText(R.string.unknown_weather);
        TextView humidity = (TextView)view.findViewById(R.id.humidity);
        humidity.setText("");
        TextView wind = (TextView)view.findViewById(R.id.wind);
        wind.setText("");
        
        View tempSection = view.findViewById(R.id.temp);
        tempSection.setVisibility(View.INVISIBLE);
    }

}
