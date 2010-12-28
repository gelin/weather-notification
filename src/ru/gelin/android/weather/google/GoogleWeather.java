package ru.gelin.android.weather.google;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.WeatherException;

/**
 *  Weather, provided by Google API.
 */
public class GoogleWeather implements Weather {

    Location location;
    
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UnitSystem getUnitSystem() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<WeatherCondition> getConditions() {
        // TODO Auto-generated method stub
        return null;
    }
    
    void parse(InputStream xml) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        parser.setInput(xml, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                String data = parser.getAttributeValue(null, "data");
                if ("city".equals(tag)) {
                    this.location = new SimpleLocation(data);
                }
            }
            eventType = parser.next();
        }
    }

}
