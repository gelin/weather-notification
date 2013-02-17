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

public class PrecipitationPeriodTest extends AndroidTestCase {

    public void testValueOfInt1() {
        PrecipitationPeriod period = PrecipitationPeriod.valueOf(1);
        assertEquals(PrecipitationPeriod.PERIOD_1H, period);
    }

    public void testValueOfInt3() {
        PrecipitationPeriod period = PrecipitationPeriod.valueOf(3);
        assertEquals(PrecipitationPeriod.PERIOD_3H, period);
    }

    public void testValueOfInt2() {
        try {
            PrecipitationPeriod.valueOf(2);
            fail();
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

}
