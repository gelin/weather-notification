package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.WeatherConditionType;

import java.util.HashMap;
import java.util.Map;

/**
 *  Creates the weather condition type by the ID.
 */
public class WeatherConditionTypeFactory {

    static private Map<Integer, WeatherConditionType> ID_MAP = new HashMap<Integer, WeatherConditionType>();
    static {
        ID_MAP.put(200, WeatherConditionType.THUNDERSTORM_RAIN_LIGHT);
        ID_MAP.put(201, WeatherConditionType.THUNDERSTORM_RAIN);
        ID_MAP.put(202, WeatherConditionType.THUNDERSTORM_RAIN_HEAVY);
        ID_MAP.put(210, WeatherConditionType.THUNDERSTORM_LIGHT);
        ID_MAP.put(211, WeatherConditionType.THUNDERSTORM);
        ID_MAP.put(212, WeatherConditionType.THUNDERSTORM_HEAVY);
        ID_MAP.put(221, WeatherConditionType.THUNDERSTORM_RAGGED);
        ID_MAP.put(230, WeatherConditionType.THUNDERSTORM_DRIZZLE_LIGHT);
        ID_MAP.put(231, WeatherConditionType.THUNDERSTORM_DRIZZLE);
        ID_MAP.put(232, WeatherConditionType.THUNDERSTORM_DRIZZLE_HEAVY);

        ID_MAP.put(300, WeatherConditionType.DRIZZLE_LIGHT);
        ID_MAP.put(301, WeatherConditionType.DRIZZLE);
        ID_MAP.put(302, WeatherConditionType.DRIZZLE_HEAVY);
        ID_MAP.put(310, WeatherConditionType.DRIZZLE_RAIN_LIGHT);
        ID_MAP.put(311, WeatherConditionType.DRIZZLE_RAIN);
        ID_MAP.put(312, WeatherConditionType.DRIZZLE_RAIN_HEAVY);
        ID_MAP.put(321, WeatherConditionType.DRIZZLE_SHOWER);

        ID_MAP.put(500, WeatherConditionType.RAIN_LIGHT);
        ID_MAP.put(501, WeatherConditionType.RAIN);
        ID_MAP.put(502, WeatherConditionType.RAIN_HEAVY);
        ID_MAP.put(503, WeatherConditionType.RAIN_VERY_HEAVY);
        ID_MAP.put(504, WeatherConditionType.RAIN_EXTREME);
        ID_MAP.put(511, WeatherConditionType.RAIN_FREEZING);
        ID_MAP.put(520, WeatherConditionType.RAIN_SHOWER_LIGHT);
        ID_MAP.put(521, WeatherConditionType.RAIN_SHOWER);
        ID_MAP.put(522, WeatherConditionType.RAIN_SHOWER_HEAVY);

        ID_MAP.put(600, WeatherConditionType.SNOW_LIGHT);
        ID_MAP.put(601, WeatherConditionType.SNOW);
        ID_MAP.put(602, WeatherConditionType.SNOW_HEAVY);
        ID_MAP.put(611, WeatherConditionType.SLEET);
        ID_MAP.put(621, WeatherConditionType.SNOW_SHOWER);

        ID_MAP.put(701, WeatherConditionType.MIST);
        ID_MAP.put(711, WeatherConditionType.SMOKE);
        ID_MAP.put(721, WeatherConditionType.HAZE);
        ID_MAP.put(731, WeatherConditionType.SAND_WHIRLS);
        ID_MAP.put(741, WeatherConditionType.FOG);

        ID_MAP.put(800, WeatherConditionType.CLOUDS_CLEAR);
        ID_MAP.put(801, WeatherConditionType.CLOUDS_FEW);
        ID_MAP.put(802, WeatherConditionType.CLOUDS_SCATTERED);
        ID_MAP.put(803, WeatherConditionType.CLOUDS_BROKEN);
        ID_MAP.put(804, WeatherConditionType.CLOUDS_OVERCAST);

        ID_MAP.put(900, WeatherConditionType.TORNADO);
        ID_MAP.put(901, WeatherConditionType.TROPICAL_STORM);
        ID_MAP.put(902, WeatherConditionType.HURRICANE);
        ID_MAP.put(903, WeatherConditionType.COLD);
        ID_MAP.put(904, WeatherConditionType.HOT);
        ID_MAP.put(905, WeatherConditionType.WINDY);
        ID_MAP.put(906, WeatherConditionType.HAIL);
    }

    static public WeatherConditionType fromId(int id) {
        return ID_MAP.get(id);
    }

    private WeatherConditionTypeFactory() {
        //avoid instantiation
    }

}
