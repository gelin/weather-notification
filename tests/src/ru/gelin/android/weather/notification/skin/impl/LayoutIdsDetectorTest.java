/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin aka Gelin
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

package ru.gelin.android.weather.notification.skin.impl;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.notification.test.R;

public class LayoutIdsDetectorTest extends AndroidTestCase {

    LayoutIdsDetector detector;

    public void setUp() {
        this.detector = new LayoutIdsDetector(getContext());
    }
    
    public void testMainLayout() {
        this.detector.parse(R.layout.main);
        System.out.println(this.detector.ids);
        assertFalse(this.detector.contains(R.id.condition));
        assertFalse(this.detector.contains(R.id.temp));
        assertFalse("empty id set", this.detector.ids.isEmpty());
        assertTrue(this.detector.contains(R.id.main));
    }
    
    public void testNotificationLayout() {
        this.detector.parse(R.layout.notification);
        System.out.println(this.detector.ids);
        assertFalse(this.detector.contains(R.id.main));
        assertFalse("empty id set", this.detector.ids.isEmpty());
        assertTrue(this.detector.contains(R.id.temp));
        assertTrue(this.detector.contains(R.id.current_temp));
        assertTrue(this.detector.contains(R.id.current_temp_alt));
        assertTrue(this.detector.contains(R.id.high_temp));
        assertTrue(this.detector.contains(R.id.low_temp));
        assertTrue(this.detector.contains(R.id.condition));
        assertTrue(this.detector.contains(R.id.wind_humidity_text));
        assertTrue(this.detector.contains(R.id.forecasts_text));
    }
    

}
