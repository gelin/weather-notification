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

package ru.gelin.android.weather.v_0_2.google;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.gelin.android.weather.v_0_2.*;

/**
 *  Weather, provided by Google API.
 */
public class GoogleWeather implements Weather {

    /** Format for dates in the XML */
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /** Format for times in the XML */
    static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    
    Location location = new SimpleLocation("");
    Date date = new Date(0);
    Date time = new Date(0);
    UnitSystem unit = UnitSystem.SI;
    List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
    
    /**
     *  Creates the weather from the input stream with XML
     *  received from API.
     */
    public GoogleWeather(Reader xml) throws WeatherException {
        try {
            parse(xml);
        } catch (Exception e) {
            throw new WeatherException("cannot parse xml", e);
        }
    }
    
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
    public UnitSystem getUnitSystem() {
        return this.unit;
    }
    
    //@Override
    public List<WeatherCondition> getConditions() {
        return this.conditions;
    }
    
    //@Override
    public boolean isEmpty() {
        return this.conditions.isEmpty();
    }
    
    void parse(Reader xml) 
            throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);   //WTF??? Harmony's Expat is so...
        //factory.setFeature("http://xml.org/sax/features/namespaces", false);
        //factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        SAXParser parser = factory.newSAXParser();
        //explicitly decoding from UTF-8 because Google misses encoding in XML preamble
        parser.parse(new InputSource(xml), new ApiXmlHandler());
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
    
    class ApiXmlHandler extends DefaultHandler {
        
        HandlerState state;
        SimpleWeatherCondition condition;
        SimpleTemperature temperature;
        
        @Override
        public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
            String data = attributes.getValue("data");
            if ("city".equals(localName)) {
                GoogleWeather.this.location = new SimpleLocation(data);
            } else if ("forecast_date".equals(localName)) {
                try {
                    GoogleWeather.this.date = DATE_FORMAT.parse(data);
                } catch (ParseException e) {
                    throw new SAXException("invalid 'forecast_date' format: " + data, e);
                }
            } else if ("current_date_time".equals(localName)) {
                try {
                    GoogleWeather.this.time = TIME_FORMAT.parse(data);
                } catch (ParseException e) {
                    throw new SAXException("invalid 'current_date_time' format: " + data, e);
                }
            } else if ("unit_system".equals(localName)) {
                GoogleWeather.this.unit = UnitSystem.valueOf(data);
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
                if (UnitSystem.US.equals(GoogleWeather.this.unit)) {
                    try {
                        temperature.setCurrent(Integer.parseInt(data), UnitSystem.US);
                    } catch (NumberFormatException e) {
                        throw new SAXException("invalid 'temp_f' format: " + data, e);
                    }
                }
            } else if ("temp_c".equals(localName)) {
                if (UnitSystem.SI.equals(GoogleWeather.this.unit)) {
                    try {
                        temperature.setCurrent(Integer.parseInt(data), UnitSystem.SI);
                    } catch (NumberFormatException e) {
                        throw new SAXException("invalid 'temp_c' format: " + data, e);
                    }
                }
            } else if ("humidity".equals(localName)) {
                condition.setHumidityText(data);
            } else if ("wind_condition".equals(localName)) {
                condition.setWindText(data);
            } else if ("low".equals(localName)) {
                try {
                    temperature.setLow(Integer.parseInt(data), GoogleWeather.this.unit);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'low' format: " + data, e);
                }
            } else if ("high".equals(localName)) {
                try {
                    temperature.setHigh(Integer.parseInt(data), GoogleWeather.this.unit);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'high' format: " + data, e);
                }
            }
        }
        
        //@Override
        //public void endElement(String uri, String localName, String qName)
        //        throws SAXException {
        //    boolean dummy = true;
        //}
        
        void addCondition() {
            condition = new SimpleWeatherCondition();
            temperature = new SimpleTemperature(GoogleWeather.this.unit);
            condition.setTemperature(temperature);
            GoogleWeather.this.conditions.add(condition);
        }

    }

}
