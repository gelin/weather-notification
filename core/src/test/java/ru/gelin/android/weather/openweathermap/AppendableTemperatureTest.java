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

import org.junit.Test;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;

import static org.junit.Assert.assertEquals;


public class AppendableTemperatureTest {

    @Test
    public void testAppendLowTemperature() {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temperature.getLow());
        SimpleTemperature append1 = new SimpleTemperature(TemperatureUnit.C);
        append1.setLow(-5, TemperatureUnit.C);
        temperature.append(append1);
        assertEquals(-5, temperature.getLow());
        SimpleTemperature append2 = new SimpleTemperature(TemperatureUnit.C);
        append2.setLow(-10, TemperatureUnit.C);
        temperature.append(append2);
        assertEquals(-10, temperature.getLow());
        SimpleTemperature append3 = new SimpleTemperature(TemperatureUnit.C);
        append3.setLow(-3, TemperatureUnit.C);
        temperature.append(append3);
        assertEquals(-10, temperature.getLow());
    }

    @Test
    public void testAppendHighTemperature() {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temperature.getHigh());
        SimpleTemperature append1 = new SimpleTemperature(TemperatureUnit.C);
        append1.setHigh(5, TemperatureUnit.C);
        temperature.append(append1);
        assertEquals(5, temperature.getHigh());
        SimpleTemperature append2 = new SimpleTemperature(TemperatureUnit.C);
        append2.setHigh(10, TemperatureUnit.C);
        temperature.append(append2);
        assertEquals(10, temperature.getHigh());
        SimpleTemperature append3 = new SimpleTemperature(TemperatureUnit.C);
        append3.setHigh(3, TemperatureUnit.C);
        temperature.append(append3);
        assertEquals(10, temperature.getHigh());
    }

    @Test
    public void testAppendTemperatureConvert() {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temperature.getLow());
        SimpleTemperature append1 = new SimpleTemperature(TemperatureUnit.K);
        append1.setLow(273, TemperatureUnit.K);
        temperature.append(append1);
        assertEquals(0, temperature.getLow());
        assertEquals(0, temperature.getHigh());
    }

}
