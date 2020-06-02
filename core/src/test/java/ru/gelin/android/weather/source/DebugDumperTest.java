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

package ru.gelin.android.weather.source;

import android.content.Context;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class DebugDumperTest {

    private final Context context = ApplicationProvider.getApplicationContext();
    private DebugDumper dumper;
    private String now;

    @Before
    public void setUp() {
        this.dumper = new DebugDumper(context, "http://openweathermap.org/data/2.5");
        this.now = DebugDumper.DATE_FORMAT.format(new Date());
    }

    @Test
    public void testGetDumpFile1() {
        String url = "http://openweathermap.org/data/2.5/weather?q=test";
        String fileName = this.dumper.getDumpFile(url).getName();
        assertTrue(this.now.compareTo(fileName) < 0);
        assertEquals("_weather_q_test.txt", fileName.substring(20));
    }

    @Test
    public void testGetDumpFile2() {
        String url = "http://openweathermap.org/data/2.5/weather?q=omsk&lat=54.96&lon=73.38";
        String fileName = this.dumper.getDumpFile(url).getName();
        assertTrue(this.now.compareTo(fileName) < 0);
        assertEquals("_weather_q_omsk_lat_54.96_lon_73.38.txt", fileName.substring(20));
    }

    @Test
    public void testGetDumpFile3() {
        String url = "http://openweathermap.org/data/2.5/forecast/daily?cnt=4&id=123456";
        String fileName = this.dumper.getDumpFile(url).getName();
        assertTrue(this.now.compareTo(fileName) < 0);
        assertEquals("_forecast_daily_cnt_4_id_123456.txt", fileName.substring(20));
    }

}
