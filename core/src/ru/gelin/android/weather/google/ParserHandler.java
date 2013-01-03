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
import ru.gelin.android.weather.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("deprecation")
class ParserHandler extends DefaultHandler {
    
    protected GoogleWeather weather;
    
    protected SimpleWeatherCondition condition;
    protected SimpleTemperature temperature;
    protected GoogleHumidity humidity;
    protected GoogleWind wind;
    
    protected HandlerState state;
    protected int conditionCounter = 0;

    /** Format for dates in the XML */
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /** Format for times in the XML */
    static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    public ParserHandler(GoogleWeather weather) {
        this.weather = weather;
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
        String data = attributes.getValue("data");
        if ("city".equals(localName)) {
            this.weather.location = new SimpleLocation(data);
        } else if ("forecast_date".equals(localName)) {
            try {
                this.weather.date = DATE_FORMAT.parse(data);
            } catch (ParseException e) {
                throw new SAXException("invalid 'forecast_date' format: " + data, e);
            }
        } else if ("current_date_time".equals(localName)) {
            try {
                this.weather.time = TIME_FORMAT.parse(data);
            } catch (ParseException e) {
                throw new SAXException("invalid 'current_date_time' format: " + data, e);
            }
        } else if ("unit_system".equals(localName)) {
            if (data.equalsIgnoreCase("si")) {
                this.weather.unit = UnitSystem.SI;
            } else {
                this.weather.unit = UnitSystem.US;
            }
        } else if ("current_conditions".equals(localName)) {
            this.state = HandlerState.CURRENT_CONDITIONS;
            addCondition();
        } else if ("forecast_conditions".equals(localName)) {
            switch (state) {
            case CURRENT_CONDITIONS:
                this.state = HandlerState.FIRST_FORECAST;
                break;
            case FIRST_FORECAST:
                this.state = HandlerState.NEXT_FORECAST;
                addCondition();
                break;
            default:
                addCondition();
            }
        } else if ("condition".equals(localName)) {
            switch (this.state) {
            case FIRST_FORECAST:
                //skipping update of condition, because the current conditions are already set
                break;
            default:
                this.condition.setConditionText(data);
            }
        } else if ("temp_f".equalsIgnoreCase(localName)) {
            if (UnitSystem.US.equals(weather.unit)) {
                try {
                    this.temperature.setCurrent(Integer.parseInt(data), TemperatureUnit.F);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'temp_f' format: " + data, e);
                }
            }
        } else if ("temp_c".equals(localName)) {
            if (UnitSystem.SI.equals(weather.unit)) {
                try {
                    this.temperature.setCurrent(Integer.parseInt(data), TemperatureUnit.C);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'temp_c' format: " + data, e);
                }
            }
        } else if ("low".equals(localName)) {
            try {
                this.temperature.setLow(Integer.parseInt(data), this.weather.unit);
            } catch (NumberFormatException e) {
                throw new SAXException("invalid 'low' format: " + data, e);
            }
        } else if ("high".equals(localName)) {
            try {
                this.temperature.setHigh(Integer.parseInt(data), this.weather.unit);
            } catch (NumberFormatException e) {
                throw new SAXException("invalid 'high' format: " + data, e);
            }
        } else if ("humidity".equals(localName)) {
            this.humidity.setText(data);     //sets translated text, for backward compatibility
        } else if ("wind_condition".equals(localName)) {
            this.wind.setText(data);    //sets translated text, for backward compatibility 
        } 
    }

    protected void addCondition() {
        if (this.weather.conditions.size() <= this.conditionCounter) {
            this.condition = new SimpleWeatherCondition();
            this.temperature = new SimpleTemperature(weather.unit);
            this.humidity = new GoogleHumidity();
            this.wind = new GoogleWind();
            this.condition.setTemperature(this.temperature);
            this.condition.setHumidity(this.humidity);
            this.condition.setWind(this.wind);
            this.weather.conditions.add(this.condition);
        } else {
            this.condition = (SimpleWeatherCondition)
                    this.weather.conditions.get(this.conditionCounter);
            //convert the temperature to the new unit
            this.temperature = (SimpleTemperature)this.condition.getTemperature(weather.unit);
            this.condition.setTemperature(this.temperature);
            this.humidity = (GoogleHumidity)this.condition.getHumidity();
            //by default parse in mph
            this.wind = (GoogleWind)this.condition.getWind(WindSpeedUnit.MPH);
            //this.condition.setHumidity(this.humidity);
            //this.condition.setWind(this.wind);
        }
        this.conditionCounter++;
    }
}
