/*
 *  Weather API.
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

import org.xml.sax.SAXException;
import ru.gelin.android.weather.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Weather, provided by Google API.
 */
@SuppressWarnings("deprecation")
public class GoogleWeather implements Weather {

    Location location = new SimpleLocation("");
    Date date = new Date(0);
    Date time = new Date(0);
    Date queryTime = new Date();
    UnitSystem unit = UnitSystem.US;
    List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
    
    //@Override
    public Location getLocation() {
        return this.location;
    }
    
    //@Override
    public Date getTime() {
        if (this.time.after(this.date)) {   //sometimes time is 0, but the date has correct value
            return this.time;
        } else {
            return this.date;
        }
    }
    
    public Date getQueryTime() {
        return this.queryTime;
    }
    
    //@Override
    @Deprecated
    public UnitSystem getUnitSystem() {
        return this.unit;
    }
    
    //@Override
    public List<WeatherCondition> getConditions() {
        return new ArrayList<WeatherCondition>(this.conditions);
    }
    
    //@Override
    public boolean isEmpty() {
        return this.conditions.isEmpty();
    }

    public URL getForecastURL() {
        return null;
    }

    /**
     *  Sets the location.
     *  Used by the weather source if the location, returned from API is empty.
     */
    void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     *  Test method, creates the weather from two readers: english and localized XMLs.
     */
    public static GoogleWeather parse(Reader enXml, Reader xml) 
            throws IOException, SAXException, ParserConfigurationException {
        GoogleWeather weather = new GoogleWeather();
        GoogleWeatherParser parser = new GoogleWeatherParser(weather);
        parser.parse(enXml, new EnglishParserHandler(weather));
        parser.parse(xml, new ParserHandler(weather));
        return weather;
    }

}
