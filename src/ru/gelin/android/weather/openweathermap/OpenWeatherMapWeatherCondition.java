package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.*;

import java.util.EnumSet;
import java.util.Set;

/**
 *  Special condition to hold additional data available by OpenWeatherMap.
 */
public class OpenWeatherMapWeatherCondition extends SimpleWeatherCondition {

    SimplePrecipitation precipitation;
    SimpleCloudiness cloudiness;
    Set<WeatherConditionType> conditionTypes = EnumSet.noneOf(WeatherConditionType.class);

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

    public Cloudiness getCloudiness(CloudinessUnit unit) {
        return this.cloudiness.convert(unit);
    }

    public Set<WeatherConditionType> getConditionTypes() {
        return EnumSet.copyOf(this.conditionTypes);
    }

    public void addConditionType(WeatherConditionType type) {
        this.conditionTypes.add(type);
    }
}
