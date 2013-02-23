/*
 *  Android Weather Notification.
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

import android.test.AndroidTestCase;

@SuppressWarnings("deprecation")
public class SimpleWeatherConditionTest extends AndroidTestCase {
    
    SimpleWeatherCondition condition;
    
    public void setUp() {
        condition = new SimpleWeatherCondition();
    }
    
    public void testTempInUnits() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setCurrent(25, UnitSystem.SI);
        condition.setTemperature(temp);
        Temperature temp1 = condition.getTemperature();
        assertEquals(25, temp1.getCurrent());
        Temperature temp2 = condition.getTemperature(UnitSystem.US);
        assertEquals(77, temp2.getCurrent());
    }

    public void testConditionTypePriority() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.CLOUDS_BROKEN);
        condition.addConditionType(WeatherConditionType.RAIN);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    public void testConditionTypeStrength() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.RAIN);
        condition.addConditionType(WeatherConditionType.RAIN_EXTREME);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN_EXTREME));
        assertFalse(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    public void testConditionTypeStrengthAndPriority() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.CLOUDS_BROKEN);
        condition.addConditionType(WeatherConditionType.RAIN_EXTREME);
        condition.addConditionType(WeatherConditionType.RAIN);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN_EXTREME));
        assertFalse(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    public void testConditionTypeSameStrength() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.addConditionType(WeatherConditionType.TORNADO);
        condition.addConditionType(WeatherConditionType.HURRICANE);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.TORNADO));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.HURRICANE));
    }

}
