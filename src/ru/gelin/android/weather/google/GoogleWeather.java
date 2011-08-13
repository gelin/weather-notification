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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.WindSpeedUnit;

/**
 *  Weather, provided by Google API.
 */
@SuppressWarnings("deprecation")
public class GoogleWeather implements Weather {

    Location location = new SimpleLocation("");
    Date date = new Date(0);
    Date time = new Date(0);
    TemperatureUnit tunit = TemperatureUnit.C;
    WindSpeedUnit wsunit = WindSpeedUnit.MPH;
    List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
    
    /**
     *  Creates the weather from the input stream with XML
     *  received from API.
     */
    /*public GoogleWeather(Reader xml1, Reader xml2) throws WeatherException {
        try {
            parse(xml1, xml2);
        } catch (Exception e) {
            throw new WeatherException("cannot parse user xml", e);
        }
    }*/
    
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
    
    //@Override
    @Deprecated
    public UnitSystem getUnitSystem() {
        //TODO: must return reasonable value
        return null;
    }
    
    //@Override
    public TemperatureUnit getTemperatureUnit() {
        return this.tunit;
    }
    
    //@Override
    public WindSpeedUnit getWindSpeedUnit() {
        return this.wsunit;
    }
    
    //@Override
    public List<WeatherCondition> getConditions() {
        return this.conditions;
    }
    
    //@Override
    public boolean isEmpty() {
        return this.conditions.isEmpty();
    }

    /**
     *  Sets the location.
     *  Used by the weather source if the location, returned from API is empty.
     */
    void setLocation(Location location) {
        this.location = location;
    }
    
    static enum HandlerState {
        CURRENT_CONDITIONS, FIRST_FORECAST, NEXT_FORECAST;
    }
    
    

}
