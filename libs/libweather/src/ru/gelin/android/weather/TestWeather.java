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

package ru.gelin.android.weather;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *  Simple weather realization. Holds only one temperature value. Used for tests.
 */
public class TestWeather implements Weather {

    /** Location */
    Location location = new SimpleLocation("Test location");
    /** Time */
    Date time = new Date();
    /** List of conditions */
    List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();

    public TestWeather(int temperature) {
        this.conditions.add(createCondition(temperature));
        this.conditions.add(createCondition(temperature));
        this.conditions.add(createCondition(temperature));
        this.conditions.add(createCondition(temperature));
    }

    SimpleWeatherCondition createCondition(int temp) {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        SimpleTemperature temperature = new SimpleTemperature(TemperatureUnit.C);
        temperature.setCurrent(temp, TemperatureUnit.C);
        condition.setTemperature(temperature);
        SimpleHumidity humidity = new SimpleHumidity();
        humidity.setValue(0);
        condition.setHumidity(humidity);
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPS);
        condition.setWind(wind);
        condition.setConditionText("Test weather");
        return condition;
    }

    //@Override
    public Location getLocation() {
        return this.location;
    }

    //@Override
    public Date getTime() {
        return this.time;
    }
    
    public Date getQueryTime() {
        return this.time;
    }

    //@Override
    @Deprecated
    public UnitSystem getUnitSystem() {
        return UnitSystem.SI;
    }
    
    //@Override
    public List<WeatherCondition> getConditions() {
        return Collections.unmodifiableList(new ArrayList<WeatherCondition>(this.conditions));
    }
    
    //@Override
    public boolean isEmpty() {
        return false;
    }

    public URL getForecastURL() {
        return null;
    }

}
