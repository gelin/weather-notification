package ru.gelin.android.weather.openweathermap;

import android.content.Context;
import ru.gelin.android.weather.notification.R;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static ru.gelin.android.weather.openweathermap.WeatherConditionType.*;

/**
 *  Formats weather condition text.
 */
public class WeatherConditionFormat {

    static final String SEPARATOR = ", ";

    static Map<WeatherConditionType, Integer> RES_MAP = new EnumMap<WeatherConditionType, Integer>(WeatherConditionType.class);
    static {
        RES_MAP.put(THUNDERSTORM_RAIN_LIGHT, R.string.condition_thunderstorm_rain_light);
        RES_MAP.put(THUNDERSTORM_RAIN, R.string.condition_thunderstorm_rain);
        RES_MAP.put(THUNDERSTORM_RAIN_HEAVY, R.string.condition_thunderstorm_rain_heavy);
        RES_MAP.put(THUNDERSTORM_LIGHT, R.string.condition_thunderstorm_light);
        RES_MAP.put(THUNDERSTORM, R.string.condition_thunderstorm);
        RES_MAP.put(THUNDERSTORM_HEAVY, R.string.condition_thunderstorm_heavy);
        RES_MAP.put(THUNDERSTORM_RAGGED, R.string.condition_thunderstorm_ragged);
        RES_MAP.put(THUNDERSTORM_DRIZZLE_LIGHT, R.string.condition_thunderstorm_drizzle_light);
        RES_MAP.put(THUNDERSTORM_DRIZZLE, R.string.condition_thunderstorm_drizzle);
        RES_MAP.put(THUNDERSTORM_DRIZZLE_HEAVY, R.string.condition_thunderstorm_drizzle_heavy);

        RES_MAP.put(DRIZZLE_LIGHT, R.string.condition_drizzle_light);
        RES_MAP.put(DRIZZLE, R.string.condition_drizzle);
        RES_MAP.put(DRIZZLE_HEAVY, R.string.condition_drizzle_heavy);
        RES_MAP.put(DRIZZLE_RAIN_LIGHT, R.string.condition_drizzle_rain_light);
        RES_MAP.put(DRIZZLE_RAIN, R.string.condition_drizzle_rain);
        RES_MAP.put(DRIZZLE_RAIN_HEAVY, R.string.condition_drizzle_rain_heavy);
        RES_MAP.put(DRIZZLE_SHOWER, R.string.condition_drizzle_shower);

        RES_MAP.put(RAIN_LIGHT, R.string.condition_rain_light);
        RES_MAP.put(RAIN, R.string.condition_rain);
        RES_MAP.put(RAIN_HEAVY, R.string.condition_rain_heavy);
        RES_MAP.put(RAIN_VERY_HEAVY, R.string.condition_rain_very_heavy);
        RES_MAP.put(RAIN_EXTREME, R.string.condition_rain_extreme);
        RES_MAP.put(RAIN_FREEZING, R.string.condition_rain_freezing);
        RES_MAP.put(RAIN_SHOWER_LIGHT, R.string.condition_rain_shower_light);
        RES_MAP.put(RAIN_SHOWER, R.string.condition_rain_shower);
        RES_MAP.put(RAIN_SHOWER_HEAVY, R.string.condition_rain_shower_heavy);

        RES_MAP.put(SNOW_LIGHT, R.string.condition_snow_light);
        RES_MAP.put(SNOW, R.string.condition_snow);
        RES_MAP.put(SNOW_HEAVY, R.string.condition_snow_heavy);
        RES_MAP.put(SLEET, R.string.condition_sleet);
        RES_MAP.put(SNOW_SHOWER, R.string.condition_snow_shower);

        RES_MAP.put(MIST, R.string.condition_mist);
        RES_MAP.put(SMOKE, R.string.condition_smoke);
        RES_MAP.put(HAZE, R.string.condition_haze);
        RES_MAP.put(SAND_WHIRLS, R.string.condition_sand_whirls);
        RES_MAP.put(FOG, R.string.condition_fog);

        RES_MAP.put(CLOUDS_CLEAR, R.string.condition_clouds_clear);
        RES_MAP.put(CLOUDS_FEW, R.string.condition_clouds_few);
        RES_MAP.put(CLOUDS_SCATTERED, R.string.condition_clouds_scattered);
        RES_MAP.put(CLOUDS_BROKEN, R.string.condition_clouds_broken);
        RES_MAP.put(CLOUDS_OVERCAST, R.string.condition_clouds_overcast);

        RES_MAP.put(TORNADO, R.string.condition_tornado);
        RES_MAP.put(TROPICAL_STORM, R.string.condition_tropical_storm);
        RES_MAP.put(HURRICANE, R.string.condition_hurricane);
        RES_MAP.put(COLD, R.string.condition_cold);
        RES_MAP.put(HOT, R.string.condition_hot);
        RES_MAP.put(WINDY, R.string.condition_windy);
        RES_MAP.put(HAIL, R.string.condition_hail);
    }

    Context context;

    public WeatherConditionFormat(Context context) {
        this.context = context;
    }

    public String getText(OpenWeatherMapWeatherCondition condition) {
        int maxPriority = 0;
        List<WeatherConditionType> resultTypes = new ArrayList<WeatherConditionType>();
        for (WeatherConditionType type : condition.getConditionTypes()) {
            if (type.getPriority() > maxPriority) {
                maxPriority = type.getPriority();
            }
        }
        for (WeatherConditionType type : condition.getConditionTypes()) {
            if (type.getPriority() == maxPriority) {
                resultTypes.add(type);
            }
        }
        if (resultTypes.isEmpty()) {
            return this.context.getString(getStringId(WeatherConditionType.CLOUDS_CLEAR));
        }
        StringBuilder result = new StringBuilder();
        for (WeatherConditionType type : resultTypes) {
            Integer id = getStringId(type);
            if (id != null) {
                result.append(this.context.getString(id));
                result.append(SEPARATOR);
            }
        }
        if (result.length() > 0) {
            result.delete(result.length() - SEPARATOR.length(), result.length());
        }
        return result.toString();
    }

    static Integer getStringId(WeatherConditionType type) {
        return RES_MAP.get(type);
    }

}
