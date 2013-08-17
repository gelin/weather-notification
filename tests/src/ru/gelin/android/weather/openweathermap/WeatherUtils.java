package ru.gelin.android.weather.openweathermap;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ru.gelin.android.weather.Weather;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class WeatherUtils {
    
    public static Weather createOpenWeather(Context context) throws Exception {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context, readJSON("omsk_name_2.5.json"));
        weather.parseDailyForecast(readJSON("omsk_name_forecast_2.5.json"));
        return weather;
    }

    public static Weather createIncompleteOpenWeather(Context context) throws Exception {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(context, readJSON("omsk_name_2.5.json"));
        return weather;
    }

    public static JSONObject readJSON(String resourceName) throws IOException, JSONException {
        Reader reader = new InputStreamReader(WeatherUtils.class.getResourceAsStream(resourceName));
        StringBuilder buffer = new StringBuilder();
        int c = reader.read();
        while (c >= 0) {
            buffer.append((char)c);
            c = reader.read();
        }
        JSONTokener parser = new JSONTokener(buffer.toString());
        return (JSONObject)parser.nextValue();
    }

}
