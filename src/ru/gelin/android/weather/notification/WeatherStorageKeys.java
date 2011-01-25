package ru.gelin.android.weather.notification;

public interface WeatherStorageKeys {

    /** Preference name for location. */
    static final String LOCATION = "weather_location";
    /** Preference name for time. */
    static final String TIME = "weather_time";
    /** Preference name for unit system. */
    static final String UNIT_SYSTEM = "weather_unit_system";
    /** Preference name pattern for condition text. */
    static final String CONDITION_TEXT = "weather_%d_condition_text";
    /** Preference name pattern for current temp. */
    static final String CURRENT_TEMP = "weather_%d_current_temp";
    /** Preference name pattern for low temp. */
    static final String LOW_TEMP = "weather_%d_low_temp";
    /** Preference name pattern for high temp. */
    static final String HIGH_TEMP = "weather_%d_high_temp";
    /** Preference name pattern for humidity text. */
    static final String HUMIDITY_TEXT = "weather_%d_humidity_text";
    /** Preference name pattern for wind text. */
    static final String WIND_TEXT = "weather_%d_wind_text";

}
