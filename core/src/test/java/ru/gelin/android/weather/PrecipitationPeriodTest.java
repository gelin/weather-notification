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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PrecipitationPeriodTest {

    @Test
    public void testValueOfInt1() {
        PrecipitationPeriod period = PrecipitationPeriod.valueOf(1);
        assertEquals(PrecipitationPeriod.PERIOD_1H, period);
    }

    @Test
    public void testValueOfInt3() {
        PrecipitationPeriod period = PrecipitationPeriod.valueOf(3);
        assertEquals(PrecipitationPeriod.PERIOD_3H, period);
    }

    @Test
    public void testValueOfInt2() {
        try {
            PrecipitationPeriod.valueOf(2);
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

}
