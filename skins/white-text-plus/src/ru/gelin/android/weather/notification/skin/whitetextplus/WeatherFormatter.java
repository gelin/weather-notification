package ru.gelin.android.weather.notification.skin.whitetextplus;

import android.content.Context;
import ru.gelin.android.weather.Weather;

/**
 *  A special weather formatter.
 */
public class WeatherFormatter extends ru.gelin.android.weather.notification.skin.impl.WeatherFormatter {

    public WeatherFormatter(Context context, Weather weather) {
        super(context, weather);
    }

    @Override
    protected TemperatureFormat getTemperatureFormat() {
        return new TemperatureFormat();
    }
}
