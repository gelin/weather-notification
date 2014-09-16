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

import android.test.InstrumentationTestCase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.notification.WeatherUtils;
import ru.gelin.android.weather.source.HttpUtils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class GoogleWeatherSourceTest extends InstrumentationTestCase {

    //ignoring test because Google API is not available now
    public void ignoretestQueryRu() throws Exception {
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

    //ignoring test because Google API is not available now
    public void ignoretestRawQueryRu() throws Exception {
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
        String charset = HttpUtils.getCharset(entity);
        System.out.println(response.getAllHeaders());
        Reader in = new InputStreamReader(entity.getContent(), charset);
        int c;
        while ((c = in.read()) >= 0) {
            System.out.print((char)c);
        }
        assertTrue(true);
    }

    //ignoring test because Google API is not available now
    public void ignoretestQueryTime() throws WeatherException {
        Date now = new Date();
        WeatherSource source = new GoogleWeatherSource();
        Location location = new SimpleLocation("Омск");
        Weather weather = source.query(location, new Locale("ru"));
        assertNotNull(weather.getQueryTime());
        assertTrue(!weather.getQueryTime().before(now));
    }

    public void testDefaultTemperatureUnit() throws Exception {
        Weather weather = WeatherUtils.createWeather(getInstrumentation().getContext());
        assertEquals(TemperatureUnit.F, weather.getConditions().get(0).getTemperature().getTemperatureUnit());
    }

    public void testDefaultWindSpeedUnit() throws Exception {
        Weather weather = WeatherUtils.createWeather(getInstrumentation().getContext());
        assertEquals(WindSpeedUnit.MPH, weather.getConditions().get(0).getWind().getSpeedUnit());
    }

}
