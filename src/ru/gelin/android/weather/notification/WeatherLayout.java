package ru.gelin.android.weather.notification;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;

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
        TextView time = (TextView)view.findViewById(R.id.update_time);
        setText(time, this.context.getString(
                R.string.update_time_format, weather.getTime()));
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
        
        Temperature temp = currentCondition.getTemperature();
        TextView currentTemp = (TextView)view.findViewById(R.id.current_temp);
        setText(currentTemp, this.context.getString(
                R.string.current_temp_format, temp.getCurrent()));
        TextView highLowTemp = (TextView)view.findViewById(R.id.high_low_temp);
        setText(highLowTemp, this.context.getString(
                R.string.high_low_temp_format, temp.getLow(), temp.getHigh()));
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

}
