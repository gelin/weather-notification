package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.SimpleWeatherCondition;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  Special condition to hold additional data available by OpenWeatherMap.
 */
public class OpenWeatherMapWeatherCondition extends SimpleWeatherCondition {

    Set<WeatherConditionType> conditionTypes = EnumSet.noneOf(WeatherConditionType.class);

    public Set<WeatherConditionType> getConditionTypes() {
        return EnumSet.copyOf(this.conditionTypes);
    }

    public void addConditionType(WeatherConditionType newType) {
        if (newType == null) {
            return;
        }
        Iterator<WeatherConditionType> i = this.conditionTypes.iterator();
        boolean insert = true;
        while (i.hasNext()) {
            WeatherConditionType type = i.next();
            if (newType.getPriority() == type.getPriority()) {
                if (newType.getStrength() > type.getStrength()) {
                    i.remove();
                    insert = true;
                } else if (newType.getStrength() < type.getStrength()) {
                    insert = false;
                }
            }
        }
        if (insert) {
            this.conditionTypes.add(newType);
        }
    }
}
