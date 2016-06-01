/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.v_0_2;

/**
 *  Simple weather condition implementation which just holds
 *  the values.
 */
public class SimpleWeatherCondition implements WeatherCondition {

    String conditionText;
    SimpleTemperature temperature;
    String windText;
    String humidityText;
    
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
    public void setWindText(String text) {
        this.windText = text;
    }
    
    /**
     *  Sets the humidity text.
     */
    public void setHumidityText(String text) {
        this.humidityText = text;
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
    public Temperature getTemperature(UnitSystem unit) {
        if (this.temperature == null) {
            return null;
        }
        if (this.temperature.getUnitSystem().equals(unit)) {
            return this.temperature;
        }
        return this.temperature.convert(unit);
    }

    //@Override
    public String getWindText() {
        return this.windText;
    }
    
    //@Override
    public String getHumidityText() {
        return this.humidityText;
    }

}
