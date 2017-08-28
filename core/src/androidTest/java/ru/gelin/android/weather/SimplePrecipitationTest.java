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

import android.test.AndroidTestCase;

@SuppressWarnings("deprecation")
public class SimplePrecipitationTest extends AndroidTestCase {

    public void testSetGetValue() {
        SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.setValue(3.0f, PrecipitationPeriod.PERIOD_3H);
        assertEquals(1.0f, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
    }

    public void testUnknownValue() {
        SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
        assertEquals(Precipitation.UNKNOWN, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
        assertEquals(Precipitation.UNKNOWN, precipitation.getValue(PrecipitationPeriod.PERIOD_3H));
    }

}
