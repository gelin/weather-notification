package ru.gelin.android.weather.google;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import org.junit.Test;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherSource;

public class GoogleWeatherSourceTest {
    
    @Test
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
    
    @Test
    public void testRawQueryRu() throws Exception {
        String apiUrl = "http://www.google.com/ig/api?weather=%s&hl=%s";
        String fullUrl = String.format(apiUrl, 
                URLEncoder.encode("Омск", "UTF-8"),
                URLEncoder.encode("ru", "UTF-8"));
        URL url = new URL(fullUrl);
        URLConnection connection = url.openConnection();
        //connection.addRequestProperty("Accept-Charset", "UTF-8");
        //connection.addRequestProperty("Accept-Language", "ru");
        System.out.println(connection.getHeaderFields());
        Reader in = new InputStreamReader(
                connection.getInputStream(), GoogleWeatherSource.getCharset(connection));
        int c;
        while ((c = in.read()) >= 0) {
            System.out.print((char)c);
        }
        assertTrue(true);
    }
    
    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", GoogleWeatherSource.getCharset("text/xml; charset=UTF-8"));
        assertEquals("windows-1251", GoogleWeatherSource.getCharset("text/xml; charset=windows-1251"));
        assertEquals("UTF-8", GoogleWeatherSource.getCharset("text/xml"));
        assertEquals("windows-1251", GoogleWeatherSource.getCharset("text/xml; charset=windows-1251; something more"));
        assertEquals("UTF-8", GoogleWeatherSource.getCharset(""));
        assertEquals("UTF-8", GoogleWeatherSource.getCharset((String)null));
    }

}
