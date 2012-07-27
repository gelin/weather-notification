/*
 *  Weather API.
 *  Copyright (C) 2010  Denis Nelubin, Vladimir Kubyshev
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

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherException;
import ru.gelin.android.weather.WeatherSource;
import ru.gelin.android.weather.source.HttpWeatherSource;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 *  Weather source which takes weather from the Google API.
 */
public class GoogleWeatherSource extends HttpWeatherSource implements WeatherSource {

    /** API URL */
    static final String API_URL = "http://www.google.com/ig/api?weather=%s&hl=%s";

    GoogleWeatherParser weatherParser;
    GoogleWeather weather;

    //@Override
    public Weather query(Location location) throws WeatherException {
        return query(location, Locale.getDefault());
    }

    void parseContent(Location location, String locale, DefaultHandler handler)
            throws WeatherException, SAXException, ParserConfigurationException, IOException {
        String fullUrl;
        try {
            fullUrl = String.format(API_URL, 
                    URLEncoder.encode(location.getQuery(), ENCODING),
                    URLEncoder.encode(locale, ENCODING));
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(uee);    //should never happen
        }

        InputStreamReader reader = getReaderForURL(fullUrl);
        GoogleWeatherParser weatherParser = new GoogleWeatherParser(weather);
        weatherParser.parse(reader, handler);
    }

    //@Override
    public Weather query(Location location, Locale locale)
            throws WeatherException {
        try {
            weather = new GoogleWeather();
            weatherParser = new GoogleWeatherParser(weather);
            parseContent(location, "us", new EnglishParserHandler(weather));
            parseContent(location, locale.getLanguage(), new ParserHandler(weather));

            if (weather.getLocation().isEmpty()) {
                weather.setLocation(location);  //set original location
            }
            return weather;
        } catch (Exception e) {
            throw new WeatherException("create weather error", e);
        }
    }

}
