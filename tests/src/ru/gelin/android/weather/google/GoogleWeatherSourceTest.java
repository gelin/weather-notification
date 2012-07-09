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

package ru.gelin.android.weather.google;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.test.AndroidTestCase;

import ru.gelin.android.weather.notification.WeatherUtils;

@SuppressWarnings("deprecation")
public class GoogleWeatherSourceTest extends AndroidTestCase {

    public void testQueryRu() throws Exception {
        WeatherSource source = new GoogleWeatherSource();
        Location location = new SimpleLocation("Омск");
        Weather weather = source.query(location, new Locale("ru"));
        assertTrue(weather.getLocation().getText().contains("Omsk"));
        assertEquals(4, weather.getConditions().size());
        System.out.println(weather.getConditions().get(0).getHumidityText());
        assertTrue(weather.getConditions().get(0).getHumidityText().startsWith("Влажность"));
        System.out.println(weather.getConditions().get(0).getWindText());
        assertTrue(weather.getConditions().get(0).getWindText().startsWith("Ветер"));
    }
    
    public void testRawQueryRu() throws Exception {
        String apiUrl = "http://www.google.com/ig/api?weather=%s&hl=%s";
        String fullUrl = String.format(apiUrl, 
                URLEncoder.encode("Омск", "UTF-8"),
                URLEncoder.encode("ru", "UTF-8"));
        
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(fullUrl);
    	request.setHeader("User-Agent", "Google Weather/1.0 (Linux; Android)");
    	HttpResponse response = client.execute(request);

        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != 200) {
            assertTrue(false);
        }

        HttpEntity entity = response.getEntity();
        String charset = GoogleWeatherSource.getCharset(entity);
        System.out.println(response.getAllHeaders());
        Reader in = new InputStreamReader(entity.getContent(), charset);
        int c;
        while ((c = in.read()) >= 0) {
            System.out.print((char)c);
        }
        assertTrue(true);
    }
    
    public void testGetCharset() {
        assertEquals("UTF-8", GoogleWeatherSource.getCharset("text/xml; charset=UTF-8"));
        assertEquals("windows-1251", GoogleWeatherSource.getCharset("text/xml; charset=windows-1251"));
        assertEquals("UTF-8", GoogleWeatherSource.getCharset("text/xml"));
        assertEquals("windows-1251", GoogleWeatherSource.getCharset("text/xml; charset=windows-1251; something more"));
        assertEquals("UTF-8", GoogleWeatherSource.getCharset(""));
        assertEquals("UTF-8", GoogleWeatherSource.getCharset((String)null));
    }
    
    public void testQueryTime() throws WeatherException {
        Date now = new Date();
        WeatherSource source = new GoogleWeatherSource();
        Location location = new SimpleLocation("Омск");
        Weather weather = source.query(location, new Locale("ru"));
        assertNotNull(weather.getQueryTime());
        assertTrue(!weather.getQueryTime().before(now));
    }

    public void testDefaultTemperatureUnit() throws Exception {
        Weather weather = WeatherUtils.createWeather();
        assertEquals(TemperatureUnit.F, weather.getConditions().get(0).getTemperature().getTemperatureUnit());
    }

    public void testDefaultWindSpeedUnit() throws Exception {
        Weather weather = WeatherUtils.createWeather();
        assertEquals(WindSpeedUnit.MPH, weather.getConditions().get(0).getWind().getSpeedUnit());
    }

}
