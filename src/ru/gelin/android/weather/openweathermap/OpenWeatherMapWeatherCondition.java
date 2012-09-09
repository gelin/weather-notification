package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.Precipitation;
import ru.gelin.android.weather.SimpleWeatherCondition;

/**
 *  Special condition to hold additional data available by OpenWeatherMap.
 */
public class OpenWeatherMapWeatherCondition extends SimpleWeatherCondition {

    Precipitation precipitation;

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Precipitation getPrecipitation() {
        return this.precipitation;
    }

}
