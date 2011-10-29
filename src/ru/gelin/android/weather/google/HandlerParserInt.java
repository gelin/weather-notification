/*
 *  Weather API.
 *  Copyright (C) 2010  Vladimir Kubyshev, Denis Nelubin
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
 */

package ru.gelin.android.weather.google;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.SimpleWind;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WindSpeedUnit;

class HandlerParserInt extends DefaultHandler {     //TODO: rename to ..English
    
    HandlerState state;
    SimpleWeatherCondition condition;
    GoogleHumidity humidity;
    SimpleWind wind;
    int conditioncounter = 0;
    
    GoogleWeather weather;
    
    public HandlerParserInt(Weather weather) {      //TODO: replace with GoogleWeather
        this.weather = (GoogleWeather)weather;
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
        String data = attributes.getValue("data");
        if ("current_conditions".equals(localName)) {
            state = HandlerState.CURRENT_CONDITIONS;
            addCondition();
        } else if ("forecast_conditions".equals(localName)) {
            switch (state) {
            case CURRENT_CONDITIONS:
                state = HandlerState.FIRST_FORECAST;
                break;
            case FIRST_FORECAST:
                state = HandlerState.NEXT_FORECAST;
                addCondition();
                break;
            default:
                addCondition();
            }
        } else if ("humidity".equals(localName)) {
            humidity.parseText(data);
        } else if ("wind_condition".equals(localName)) {
            wind.setTextParse(data);
        } 
    }

    void addCondition() {
        condition = (SimpleWeatherCondition)weather.conditions.get(conditioncounter++); //TODO: check if possible to avoid cast
        humidity = (GoogleHumidity)condition.getHumidity();     //TODO: check if possible to avoid cast
        //by default parse in mph
        wind = (SimpleWind)condition.getWind(WindSpeedUnit.MPH);    //TODO: check if possible to avoid cast
        condition.setHumidity(humidity);
        condition.setWind(wind);
    }
}
