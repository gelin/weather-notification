package ru.gelin.android.weather.openweathermap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;

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
        parseCityWeather(jsonObject);
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
        return UnitSystem.SI;
    }

    @Override
    public List<WeatherCondition> getConditions() {
        return Collections.unmodifiableList(this.conditions);
    }

    @Override
    public boolean isEmpty() {
        return this.empty;
    }

    void parseCityWeather(JSONObject json) throws WeatherException {
        try {
            JSONArray list = json.getJSONArray("list");
            if (list.length() == 0) {
                this.empty = true;
                return;
            }
            JSONObject weatherJSON = list.getJSONObject(0);
            parseLocation(weatherJSON);
            parseTime(weatherJSON);
            parseCondition(weatherJSON);
        } catch (JSONException e) {
            throw new WeatherException("cannot parseCityWeather the weather", e);
        }
        this.empty = false;
    }

    private void parseLocation(JSONObject weatherJSON) throws JSONException {
        this.location = new SimpleLocation(weatherJSON.getString("name"), false);
    }

    private void parseTime(JSONObject weatherJSON) throws JSONException {
        long timestamp = weatherJSON.getLong("dt");
        this.time = new Date(timestamp * 1000);
    }

    private void parseCondition(JSONObject weatherJSON) throws JSONException {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.setConditionText(parseConditionText(weatherJSON));
        condition.setTemperature(parseTemperature(weatherJSON));
        condition.setWind(parseWind(weatherJSON));
        condition.setHumidity(parseHumidity(weatherJSON));
        this.conditions.add(condition);
    }

    private String parseConditionText(JSONObject weatherJSON) throws JSONException {
        double cloudiness = 0.0;
        try {
            cloudiness = weatherJSON.getJSONObject("clouds").getDouble("all");
        } catch (JSONException e) {
            //no clouds
        }
        double precipitations = 0.0;
        try {
            precipitations = weatherJSON.getJSONObject("rain").getDouble("3h") / 3.0;
        } catch (JSONException e) {
            //no rain
        }
        return String.format("Clouds: %.0f%%, Prec.: %.1f mm/h",
                cloudiness, precipitations);
                //TODO: more smart, more human-readable, localized
    }

    private SimpleTemperature parseTemperature(JSONObject weatherJSON) throws JSONException {
        SimpleTemperature temperature = new SimpleTemperature(TemperatureUnit.K);
        JSONObject main = weatherJSON.getJSONObject("main");
        double currentTemp = main.getDouble("temp");
        double minTemp = main.getDouble("temp_min");
        double maxTemp = main.getDouble("temp_max");
        temperature.setCurrent((int)currentTemp, TemperatureUnit.K);
        temperature.setLow((int)minTemp, TemperatureUnit.K);
        temperature.setHigh((int)maxTemp, TemperatureUnit.K);
        return temperature;
    }

    private SimpleWind parseWind(JSONObject weatherJSON) throws JSONException {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPS);
        JSONObject windJSON = weatherJSON.getJSONObject("wind");
        double speed = windJSON.getDouble("speed");
        double deg = windJSON.getDouble("deg");
        wind.setSpeed((int)speed, WindSpeedUnit.MPS);
        wind.setDirection(WindDirection.valueOf((int) deg));
        wind.setText(String.format("Wind: %s, %d mph", String.valueOf(wind.getDirection()), wind.getSpeed()));
            //TODO: more smart, localized
        return wind;
    }

    private SimpleHumidity parseHumidity(JSONObject weatherJSON) throws JSONException {
        SimpleHumidity humidity = new SimpleHumidity();
        double humidityValue = weatherJSON.getJSONObject("main").getDouble("humidity");
        humidity.setValue((int)humidityValue);
        humidity.setText(String.format("Humidity: %d%%", humidity.getValue()));
            //TODO: more smart, localized
        return humidity;
    }

}
