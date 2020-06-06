package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.Location;

public class OpenWeatherMapCityLocation implements Location {

    static final String QUERY_TEMPLATE = "lat=%s&lon=%s";

    private final String name;
    private final double latitude;
    private final double longitude;

    public OpenWeatherMapCityLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean isEmpty() {
        return this.name == null;
    }

    @Override
    public boolean isGeo() {
        return true;
    }

    @Override
    public String getText() {
        return this.name;
    }

    @Override
    public String getQuery() {
        return String.format(QUERY_TEMPLATE, this.latitude, this.longitude);
    }

}
