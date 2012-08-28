package ru.gelin.android.weather.openweathermap;

import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Weather implementation which constructs from the JSON received from openweathermap.org
 */
public class OpenWeatherMapWeather implements Weather {

    /** JSON object over which the weather is wrapped */
    JSONObject json;

    public OpenWeatherMapWeather(JSONObject jsonObject) {
        this.json = jsonObject;
    }

    @Override
    public Location getLocation() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getQueryTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UnitSystem getUnitSystem() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<WeatherCondition> getConditions() {
        //TODO refactor
        List<WeatherCondition> result = new ArrayList<WeatherCondition>();
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        result.add(condition);
        SimpleTemperature temperature = new SimpleTemperature(TemperatureUnit.C);   //TODO: implement kelvins
        condition.setTemperature(temperature);
        double tempValue = 0.0;
        try {
            tempValue = this.json.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("temp");
        } catch (JSONException e) {
            return result;
        }
        temperature.setCurrent((int)(tempValue - 273.15), TemperatureUnit.C);
        return result;
    }

    @Override
    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
