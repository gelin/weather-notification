package ru.gelin.android.weather.openweathermap;

import java.util.HashMap;
import java.util.Map;

/**
 *  Creates the weather condition type by the ID.
 */
public class WeatherConditionTypeFactory {

    static private Map<Integer, WeatherConditionType> IDS_MAP = new HashMap<Integer, WeatherConditionType>();
    static {
        IDS_MAP.put(200, WeatherConditionType.THUNDERSTORM_RAIN_LIGHT);
        IDS_MAP.put(201, WeatherConditionType.THUNDERSTORM_RAIN);
        IDS_MAP.put(202, WeatherConditionType.THUNDERSTORM_RAIN_HEAVY);
        IDS_MAP.put(210, WeatherConditionType.THUNDERSTORM_LIGHT);
        IDS_MAP.put(211, WeatherConditionType.THUNDERSTORM);
        IDS_MAP.put(212, WeatherConditionType.THUNDERSTORM_HEAVY);
        IDS_MAP.put(221, WeatherConditionType.THUNDERSTORM_RAGGED);
        IDS_MAP.put(230, WeatherConditionType.THUNDERSTORM_DRIZZLE_LIGHT);
        IDS_MAP.put(231, WeatherConditionType.THUNDERSTORM_DRIZZLE);
        IDS_MAP.put(232, WeatherConditionType.THUNDERSTORM_DRIZZLE_HEAVY);

        IDS_MAP.put(300, WeatherConditionType.DRIZZLE_LIGHT);
        IDS_MAP.put(301, WeatherConditionType.DRIZZLE);
        IDS_MAP.put(302, WeatherConditionType.DRIZZLE_HEAVY);
        IDS_MAP.put(310, WeatherConditionType.DRIZZLE_RAIN_LIGHT);
        IDS_MAP.put(311, WeatherConditionType.DRIZZLE_RAIN);
        IDS_MAP.put(312, WeatherConditionType.DRIZZLE_RAIN_HEAVY);
        IDS_MAP.put(321, WeatherConditionType.DRIZZLE_SHOWER);

        IDS_MAP.put(500, WeatherConditionType.RAIN_LIGHT);
        IDS_MAP.put(501, WeatherConditionType.RAIN);
        IDS_MAP.put(502, WeatherConditionType.RAIN_HEAVY);
        IDS_MAP.put(503, WeatherConditionType.RAIN_VERY_HEAVY);
        IDS_MAP.put(504, WeatherConditionType.RAIN_EXTREME);
        IDS_MAP.put(511, WeatherConditionType.RAIN_FREEZING);
        IDS_MAP.put(520, WeatherConditionType.RAIN_SHOWER_LIGHT);
        IDS_MAP.put(521, WeatherConditionType.RAIN_SHOWER);
        IDS_MAP.put(522, WeatherConditionType.RAIN_SHOWER_HEAVY);

        IDS_MAP.put(600, WeatherConditionType.SNOW_LIGHT);
        IDS_MAP.put(601, WeatherConditionType.SNOW);
        IDS_MAP.put(602, WeatherConditionType.SNOW_HEAVY);
        IDS_MAP.put(611, WeatherConditionType.SLEET);
        IDS_MAP.put(621, WeatherConditionType.SNOW_SHOWER);

        IDS_MAP.put(701, WeatherConditionType.MIST);
        IDS_MAP.put(711, WeatherConditionType.SMOKE);
        IDS_MAP.put(721, WeatherConditionType.HAZE);
        IDS_MAP.put(731, WeatherConditionType.SAND_WHIRLS);
        IDS_MAP.put(741, WeatherConditionType.FOG);

        IDS_MAP.put(800, WeatherConditionType.CLOUDS_CLEAR);
        IDS_MAP.put(801, WeatherConditionType.CLOUDS_FEW);
        IDS_MAP.put(802, WeatherConditionType.CLOUDS_SCATTERED);
        IDS_MAP.put(803, WeatherConditionType.CLOUDS_BROKEN);
        IDS_MAP.put(804, WeatherConditionType.CLOUDS_OVERCAST);

        IDS_MAP.put(900, WeatherConditionType.TORNADO);
        IDS_MAP.put(901, WeatherConditionType.TROPICAL_STORM);
        IDS_MAP.put(902, WeatherConditionType.HURRICANE);
        IDS_MAP.put(903, WeatherConditionType.COLD);
        IDS_MAP.put(904, WeatherConditionType.HOT);
        IDS_MAP.put(905, WeatherConditionType.WINDY);
        IDS_MAP.put(906, WeatherConditionType.HAIL);
    }

    WeatherConditionType fromId(int id) {
        return IDS_MAP.get(id);
    }

    private WeatherConditionTypeFactory() {
        //avoid instantiation
    }

}
