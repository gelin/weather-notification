package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.*;

/**
 *  Special condition to hold additional data available by OpenWeatherMap.
 */
public class OpenWeatherMapWeatherCondition extends SimpleWeatherCondition {

    SimplePrecipitation precipitation;
    SimpleCloudiness cloudiness;

    public void setPrecipitation(SimplePrecipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Precipitation getPrecipitation() {
        return this.precipitation;
    }

    public void setCloudiness(SimpleCloudiness cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Cloudiness getCloudiness() {
        return this.cloudiness;
    }

}
