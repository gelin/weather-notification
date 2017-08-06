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

package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;

/**
 *  The temperature, which values can be appended by some another temperature, parsed from the forecasts, for example.
 *  This temperature is updated after the append operation.
 *  The Low temperature is updated by the lowest of the current and appending Low temperatures.
 *  The High temperature is updated by the highest of the current and appending High temperatures.
 */
public class AppendableTemperature extends SimpleTemperature {

    public AppendableTemperature(TemperatureUnit unit) {
        super(unit);
    }

    public void append(Temperature temperature) {
        if (getLow() == Temperature.UNKNOWN) {
            setLow(temperature.getLow(), temperature.getTemperatureUnit());
        } else {
            setLow(Math.min(
                    getLow(), convertValue(temperature.getLow(), temperature.getTemperatureUnit())),
                    getTemperatureUnit());
        }
        if (getHigh() == Temperature.UNKNOWN) {
            setHigh(temperature.getHigh(), temperature.getTemperatureUnit());
        } else {
            setHigh(Math.max(
                    getHigh(), convertValue(temperature.getHigh(), temperature.getTemperatureUnit())),
                    getTemperatureUnit());
        }
    }

}
