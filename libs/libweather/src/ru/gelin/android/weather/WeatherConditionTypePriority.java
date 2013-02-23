package ru.gelin.android.weather;

/**
 *  Weather condition type priorities.
 */
class WeatherConditionTypePriority {

    static final int EXTREME_PRIORITY = 6;
    static final int THUNDERSTORM_PRIORITY = 5;
    static final int RAIN_PRIORITY = 4;
    static final int SNOW_PRIORITY = 3;
    static final int DRIZZLE_PRIORITY = 2;
    static final int ATMOSPHERE_PRIORITY = 1;
    static final int CLOUDS_PRIORITY = 0;

    private WeatherConditionTypePriority() {
        //avoid instantiation
    }

}
