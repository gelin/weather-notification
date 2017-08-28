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

package ru.gelin.android.weather.notification.skin.impl;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.WeatherConditionType;
import ru.gelin.android.weather.notification.skin.R;

public class WeatherConditionFormatTest extends AndroidTestCase {

    WeatherConditionFormat format;

    public void setUp() {
        format = new WeatherConditionFormat(getContext());
    }

    public void testGetStringId() {
        assertEquals(R.string.condition_rain_light, (int)WeatherConditionFormat.getStringId(WeatherConditionType.RAIN_LIGHT));
        assertEquals(R.string.condition_tornado, (int)WeatherConditionFormat.getStringId(WeatherConditionType.TORNADO));
    }

    public void testGetText() {
        testConditionText("Rain", new WeatherConditionType[]{WeatherConditionType.RAIN, WeatherConditionType.CLOUDS_BROKEN});
        testConditionText("Tornado", new WeatherConditionType[]{WeatherConditionType.RAIN, WeatherConditionType.TORNADO});
        testConditionText("Tornado, Hurricane", new WeatherConditionType[]{WeatherConditionType.TORNADO, WeatherConditionType.HURRICANE});
    }

    private void testConditionText(String expected, WeatherConditionType[] types) {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        for (WeatherConditionType type : types) {
            condition.addConditionType(type);
        }
        WeatherConditionFormat format = new WeatherConditionFormat(getContext());
        assertEquals(expected, format.getText(condition));
    }

    public void testEmptyCondition() {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        WeatherConditionFormat format = new WeatherConditionFormat(getContext());
        assertEquals("Sky is clear", format.getText(condition));
    }

}
