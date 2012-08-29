package ru.gelin.android.weather.openweathermap;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.notification.app.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *  Weather implementation which constructs from the JSON received from openweathermap.org
 */
public class OpenWeatherMapWeather implements Weather {

    /** Weather location */
    SimpleLocation location;
    /** Weather time */
    Date time;
    /** Query time */
    Date queryTime = new Date();
    /** Weather conditions */
    List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
    /** Emptyness flag */
    boolean empty = true;

    public OpenWeatherMapWeather(JSONObject jsonObject) throws WeatherException {
        parse(jsonObject);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Date getTime() {
        return new Date(this.time.getTime());
    }

    @Override
    public Date getQueryTime() {
        return new Date(this.time.getTime());
    }

    @Override
    public UnitSystem getUnitSystem() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<WeatherCondition> getConditions() {
        return Collections.unmodifiableList(this.conditions);
    }

    @Override
    public boolean isEmpty() {
        return this.empty;
    }

    void parse(JSONObject json) throws WeatherException {
        try {
            JSONObject weatherJSON = json.getJSONArray("list").getJSONObject(0);
            parseLocation(weatherJSON);
            parseTime(weatherJSON);
            parseCondition(weatherJSON);
        } catch (JSONException e) {
            throw new WeatherException("cannot parse the weather", e);
        }
        this.empty = false;
    }

    void parseLocation(JSONObject weatherJSON) throws JSONException {
        this.location = new SimpleLocation(weatherJSON.getString("name"), false);
    }

    void parseTime(JSONObject weatherJSON) throws JSONException {
        long timestamp = weatherJSON.getLong("dt");
        this.time = new Date(timestamp * 1000);
    }

    void parseCondition(JSONObject weatherJSON) throws JSONException {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        SimpleTemperature temperature = new SimpleTemperature(TemperatureUnit.K);
        condition.setTemperature(temperature);
        double tempValue = weatherJSON.getJSONObject("main").getDouble("temp");
        temperature.setCurrent((int)tempValue, TemperatureUnit.K);
        this.conditions.add(condition);
    }

}
