/*
 *  Weather API.
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

package ru.gelin.android.weather;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  Simple weather condition implementation which just holds
 *  the values.
 */
public class SimpleWeatherCondition implements WeatherCondition {

    String conditionText;
    SimpleTemperature temperature;
    SimpleWind wind;
    Humidity humidity;
    SimpleCloudiness cloudiness;
    SimplePrecipitation precipitation;
    Set<WeatherConditionType> conditionTypes = EnumSet.noneOf(WeatherConditionType.class);

    /**
     *  Sets the condition text.
     */
    public void setConditionText(String text) {
        this.conditionText = text;
    }

    /**
     *  Sets the temperature.
     */
    public void setTemperature(SimpleTemperature temp) {
        this.temperature = temp;
    }

    /**
     *  Sets the wind text.
     */
    public void setWind(SimpleWind wind) {
        this.wind = wind;
    }

    /**
     *  Sets the humidity text.
     */
    public void setHumidity(Humidity hum) {
        this.humidity = hum;
    }

    //@Override
    public String getConditionText() {
        return this.conditionText;
    }

    //@Override
    public Temperature getTemperature() {
        return this.temperature;
    }

    //@Override
    @Deprecated
    public Temperature getTemperature(UnitSystem unit) {
        if (this.temperature == null) {
            return null;
        }
        if (this.temperature.getUnitSystem().equals(unit)) {
            return this.temperature;
        }
        return this.temperature.convert(unit);
    }

    public Temperature getTemperature(TemperatureUnit unit) {
        if (this.temperature == null) {
            return null;
        }
        if (this.temperature.getTemperatureUnit().equals(unit)) {
            return this.temperature;
        }
        return this.temperature.convert(unit);
    }

    //@Deprecated
    //@Override
    public String getWindText() {
        return this.wind.getText();
    }

    //@Deprecated
    //@Override
    public String getHumidityText() {
        return this.humidity.getText();
    }

    //@override
    public Wind getWind() {
        return this.wind;
    }
    
    //@Override
    public Wind getWind(WindSpeedUnit unit) {
        if (this.wind == null) {
            return null;
        }
        if (this.wind.getSpeedUnit().equals(unit)) {
            return this.wind;
        }
        return this.wind.convert(unit);
    }

    //@Override
    public Humidity getHumidity() {
        return this.humidity;
    }

    public void setCloudiness(SimpleCloudiness cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Cloudiness getCloudiness() {
        return this.cloudiness;
    }

    public Cloudiness getCloudiness(CloudinessUnit unit) {
        if (this.cloudiness == null) {
            return null;
        }
        return this.cloudiness.convert(unit);
    }

    public void setPrecipitation(SimplePrecipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Precipitation getPrecipitation() {
        return this.precipitation;
    }

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
