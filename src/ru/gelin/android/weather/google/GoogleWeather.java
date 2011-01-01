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
    List<SimpleWeatherCondition> conditions = new ArrayList<SimpleWeatherCondition>();
    
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
        // TODO Auto-generated method stub
        return null;
    }
    
    void parse(InputStream xml) 
            throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(xml, new DefaultHandler() {
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
                        throw new SAXException("invalid current_date_time format: " + data, e);
                    }
                } else if ("unit_system".equals(qName)) {
                    GoogleWeather.this.unit = UnitSystem.valueOf(data);
                }
            }
        });
    }

}
