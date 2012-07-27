package ru.gelin.android.weather.openweathermap;

import org.json.JSONObject;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;

import java.util.Date;
import java.util.List;

/**
 *  Weather implementation which constructs from the JSON received from openweathermap.org
 */
public class OpenWeatherMapWeather implements Weather {

    public OpenWeatherMapWeather(JSONObject jsonObject) {
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
