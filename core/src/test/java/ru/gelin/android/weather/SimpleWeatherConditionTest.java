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

package ru.gelin.android.weather;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("deprecation")
public class SimpleWeatherConditionTest {

    SimpleWeatherCondition condition;

    @Before
    public void setUp() {
        condition = new SimpleWeatherCondition();
    }

    @Test
    public void testTempInUnits() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setCurrent(25, UnitSystem.SI);
        condition.setTemperature(temp);
        Temperature temp1 = condition.getTemperature();
        assertEquals(25, temp1.getCurrent());
        Temperature temp2 = condition.getTemperature(UnitSystem.US);
        assertEquals(77, temp2.getCurrent());
    }

    @Test
    public void testConditionTypePriority() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.CLOUDS_BROKEN);
        condition.addConditionType(WeatherConditionType.RAIN);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    @Test
    public void testConditionTypeStrength() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.RAIN);
        condition.addConditionType(WeatherConditionType.RAIN_EXTREME);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN_EXTREME));
        assertFalse(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    @Test
    public void testConditionTypeStrengthAndPriority() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.CLOUDS_BROKEN);
        condition.addConditionType(WeatherConditionType.RAIN_EXTREME);
        condition.addConditionType(WeatherConditionType.RAIN);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN_EXTREME));
        assertFalse(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    @Test
    public void testConditionTypeSameStrength() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.TORNADO);
        condition.addConditionType(WeatherConditionType.HURRICANE);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.TORNADO));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.HURRICANE));
    }

}
