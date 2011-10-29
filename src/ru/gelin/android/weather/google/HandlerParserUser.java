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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.gelin.android.weather.SimpleHumidity;
import ru.gelin.android.weather.SimpleWind;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.WindSpeedUnit;

@SuppressWarnings("deprecation")
public class HandlerParserUser extends DefaultHandler {
	HandlerState state;
    SimpleWeatherCondition condition;
    SimpleTemperature temperature;
    SimpleHumidity humidity;
    SimpleWind wind;

    /** Format for dates in the XML */
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /** Format for times in the XML */
    static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    
    GoogleWeather weather;
    public HandlerParserUser(Weather weather) {
    	this.weather = (GoogleWeather)weather;
    }
    
    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
        String data = attributes.getValue("data");
        if ("city".equals(localName)) {
        	weather.location = new SimpleLocation(data);
        } else if ("forecast_date".equals(localName)) {
            try {
            	weather.date = DATE_FORMAT.parse(data);
            } catch (ParseException e) {
                throw new SAXException("invalid 'forecast_date' format: " + data, e);
            }
        } else if ("current_date_time".equals(localName)) {
            try {
            	weather.time = TIME_FORMAT.parse(data);
            } catch (ParseException e) {
                throw new SAXException("invalid 'current_date_time' format: " + data, e);
            }
        } else if ("unit_system".equals(localName)) {
        	if (data.equalsIgnoreCase("si")) {
        		weather.unit = UnitSystem.SI;
        	} else {
        		weather.unit = UnitSystem.US;
        	}
        } else if ("current_conditions".equals(localName)) {
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
        } else if ("condition".equals(localName)) {
            switch (state) {
            case FIRST_FORECAST:
                //skipping update of condition, because the current conditions are already set
                break;
            default:
            	condition.setConditionText(data);
            }
        } else if ("temp_f".equalsIgnoreCase(localName)) {
            if (UnitSystem.US.equals(weather.unit)) {
                try {
                	temperature.setCurrent(Integer.parseInt(data), TemperatureUnit.F);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'temp_f' format: " + data, e);
                }
            }
        } else if ("temp_c".equals(localName)) {
            if (UnitSystem.SI.equals(weather.unit)) {
                try {
                    temperature.setCurrent(Integer.parseInt(data), TemperatureUnit.C);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'temp_c' format: " + data, e);
                }
            }
        } else if ("low".equals(localName)) {
            try {
            	temperature.setLow(Integer.parseInt(data), weather.unit);
            } catch (NumberFormatException e) {
                throw new SAXException("invalid 'low' format: " + data, e);
            }
        } else if ("high".equals(localName)) {
            try {
            	temperature.setHigh(Integer.parseInt(data), weather.unit);
            } catch (NumberFormatException e) {
                throw new SAXException("invalid 'high' format: " + data, e);
            }
        } else if ("humidity".equals(localName)) {
        	humidity.setText(data); //just text for backward compatibility
        } else if ("wind_condition".equals(localName)) {
       		wind.setText(data); //just text for backward compatibility
        } 
    }
    
    void addCondition() {
    	condition = new SimpleWeatherCondition();
    	temperature = new SimpleTemperature(weather.unit);
    	humidity = new SimpleHumidity();
    	wind = new SimpleWind(WindSpeedUnit.MPH);
    	condition.setTemperature(temperature);
    	condition.setHumidity(humidity);
    	condition.setWind(wind);
    	weather.conditions.add(condition);
    }
}
