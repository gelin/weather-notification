package ru.gelin.android.weather.openweathermap;

import android.app.Instrumentation;

import static ru.gelin.android.weather.notification.WeatherUtils.readJSON;


public class WeatherUtils {
    
    public static OpenWeatherMapWeather createOpenWeather(Instrumentation instrumentation) throws Exception {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(instrumentation.getTargetContext(),
                readJSON(instrumentation.getContext(), "omsk_name_2.5.json"));
        weather.parseDailyForecast(readJSON(instrumentation.getContext(), "omsk_name_forecast_2.5.json"));
        return weather;
    }

    public static OpenWeatherMapWeather createIncompleteOpenWeather(Instrumentation instrumentation) throws Exception {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(instrumentation.getTargetContext(),
                readJSON(instrumentation.getContext(), "omsk_name_2.5.json"));
        return weather;
    }

}
