package ru.gelin.android.weather.google;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.SimpleHumidity;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.SimpleWind;
import ru.gelin.android.weather.WindSpeedUnit;
import ru.gelin.android.weather.google.GoogleWeather.HandlerState;

public class HandlerParserInt extends DefaultHandler {
	HandlerState state;
    SimpleWeatherCondition condition;
    SimpleHumidity humidity;
    SimpleWind wind;
    int conditioncounter = 0;

    GoogleWeather weather;
    public HandlerParserInt(Weather weather) {
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
        	humidity.setTextParse(data);
        } else if ("wind_condition".equals(localName)) {
       		wind.setTextParse(data);
        } 
    }

    void addCondition() {
    	condition = (SimpleWeatherCondition)weather.conditions.get(conditioncounter++);
    	humidity = (SimpleHumidity)condition.getHumidity();
    	//by default parse in mph
    	wind = (SimpleWind)condition.getWind(WindSpeedUnit.MPH);
    	condition.setHumidity(humidity);
    	condition.setWind(wind);
    }
}
