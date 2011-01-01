package ru.gelin.android.weather.google;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.WeatherException;

/**
 *  Weather, provided by Google API.
 */
public class GoogleWeather implements Weather {

    /** Format for times in the XML */
    static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    
    Location location;
    Date time;
    UnitSystem unit;
    List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
    
    /**
     *  Creates the weather from the input stream with XML
     *  received from API.
     */
    public GoogleWeather(InputStream xml) throws WeatherException {
        try {
            parse(xml);
        } catch (Exception e) {
            throw new WeatherException("cannot parse xml", e);
        }
    }
    
    @Override
    public Location getLocation() {
        return this.location;
    }
    
    @Override
    public Date getTime() {
        return this.time;
    }

    @Override
    public UnitSystem getUnitSystem() {
        return this.unit;
    }
    
    @Override
    public List<WeatherCondition> getConditions() {
        return this.conditions;
    }
    
    void parse(InputStream xml) 
            throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(xml, new ApiXmlHandler());
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
            if ("city".equals(qName)) {
                GoogleWeather.this.location = new SimpleLocation(data);
            } else if ("current_date_time".equals(qName)) {
                try {
                    GoogleWeather.this.time = TIME_FORMAT.parse(data);
                } catch (ParseException e) {
                    throw new SAXException("invalid 'current_date_time' format: " + data, e);
                }
            } else if ("unit_system".equals(qName)) {
                GoogleWeather.this.unit = UnitSystem.valueOf(data);
            } else if ("current_conditions".equals(qName)) {
                state = HandlerState.CURRENT_CONDITIONS;
                addCondition();
            } else if ("forecast_conditions".equals(qName)) {
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
            } else if ("condition".equals(qName)) {
                switch (state) {
                case FIRST_FORECAST:
                    //skipping update of condition, because the current conditions are already set
                    break;
                default:
                    condition.setConditionText(data);
                }
            } else if ("temp_f".equalsIgnoreCase(qName)) {
                if (UnitSystem.US.equals(GoogleWeather.this.unit)) {
                    try {
                        temperature.setCurrent(Integer.parseInt(data), UnitSystem.US);
                    } catch (NumberFormatException e) {
                        throw new SAXException("invalid 'temp_f' format: " + data, e);
                    }
                }
            } else if ("temp_c".equals(qName)) {
                if (UnitSystem.SI.equals(GoogleWeather.this.unit)) {
                    try {
                        temperature.setCurrent(Integer.parseInt(data), UnitSystem.SI);
                    } catch (NumberFormatException e) {
                        throw new SAXException("invalid 'temp_c' format: " + data, e);
                    }
                }
            } else if ("humidity".equals(qName)) {
                condition.setHumidityText(data);
            } else if ("wind_condition".equals(qName)) {
                condition.setWindText(data);
            } else if ("low".equals(qName)) {
                try {
                    temperature.setLow(Integer.parseInt(data), GoogleWeather.this.unit);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'low' format: " + data, e);
                }
            } else if ("high".equals(qName)) {
                try {
                    temperature.setHigh(Integer.parseInt(data), GoogleWeather.this.unit);
                } catch (NumberFormatException e) {
                    throw new SAXException("invalid 'high' format: " + data, e);
                }
            }
        }
        
        void addCondition() {
            condition = new SimpleWeatherCondition();
            temperature = new SimpleTemperature(GoogleWeather.this.unit);
            condition.setTemperature(temperature);
            GoogleWeather.this.conditions.add(condition);
        }

    }

}
