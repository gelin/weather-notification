package ru.gelin.android.weather.source;

import android.test.AndroidTestCase;

import java.util.Date;

public class DebugDumperTest extends AndroidTestCase {

    private DebugDumper dumper;
    private String now;

    public void setUp() {
        this.dumper = new DebugDumper(getContext(), "http://openweathermap.org/data/2.5");
        this.now = DebugDumper.DATE_FORMAT.format(new Date());
    }

    public void testGetDumpFile1() {
        String url = "http://openweathermap.org/data/2.5/weather?q=test";
        String fileName = this.dumper.getDumpFile(url).getName();
        assertTrue(this.now.compareTo(fileName) < 0);
        assertEquals("_weather_q_test.txt", fileName.substring(20));
    }

    public void testGetDumpFile2() {
        String url = "http://openweathermap.org/data/2.5/weather?q=omsk&lat=54.96&lon=73.38";
        String fileName = this.dumper.getDumpFile(url).getName();
        assertTrue(this.now.compareTo(fileName) < 0);
        assertEquals("_weather_q_omsk_lat_54.96_lon_73.38.txt", fileName.substring(20));
    }

    public void testGetDumpFile3() {
        String url = "http://openweathermap.org/data/2.5/forecast/daily?cnt=4&id=123456";
        String fileName = this.dumper.getDumpFile(url).getName();
        assertTrue(this.now.compareTo(fileName) < 0);
        assertEquals("_forecast_daily_cnt_4_id_123456.txt", fileName.substring(20));
    }

}
