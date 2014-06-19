package ru.gelin.android.weather.notification.skin.impl;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.WeatherConditionType;
import ru.gelin.android.weather.notification.skin.R;

import java.util.*;

import static ru.gelin.android.weather.WeatherConditionType.*;

/**
 *  Formats weather condition text.
 */
public class WeatherConditionFormat {

    static final String SEPARATOR = ", ";

    static Map<WeatherConditionType, Integer> STR_MAP = new EnumMap<WeatherConditionType, Integer>(WeatherConditionType.class);
    static {
        STR_MAP.put(THUNDERSTORM_RAIN_LIGHT, R.string.condition_thunderstorm_rain_light);
        STR_MAP.put(THUNDERSTORM_RAIN, R.string.condition_thunderstorm_rain);
        STR_MAP.put(THUNDERSTORM_RAIN_HEAVY, R.string.condition_thunderstorm_rain_heavy);
        STR_MAP.put(THUNDERSTORM_LIGHT, R.string.condition_thunderstorm_light);
        STR_MAP.put(THUNDERSTORM, R.string.condition_thunderstorm);
        STR_MAP.put(THUNDERSTORM_HEAVY, R.string.condition_thunderstorm_heavy);
        STR_MAP.put(THUNDERSTORM_RAGGED, R.string.condition_thunderstorm_ragged);
        STR_MAP.put(THUNDERSTORM_DRIZZLE_LIGHT, R.string.condition_thunderstorm_drizzle_light);
        STR_MAP.put(THUNDERSTORM_DRIZZLE, R.string.condition_thunderstorm_drizzle);
        STR_MAP.put(THUNDERSTORM_DRIZZLE_HEAVY, R.string.condition_thunderstorm_drizzle_heavy);

        STR_MAP.put(DRIZZLE_LIGHT, R.string.condition_drizzle_light);
        STR_MAP.put(DRIZZLE, R.string.condition_drizzle);
        STR_MAP.put(DRIZZLE_HEAVY, R.string.condition_drizzle_heavy);
        STR_MAP.put(DRIZZLE_RAIN_LIGHT, R.string.condition_drizzle_rain_light);
        STR_MAP.put(DRIZZLE_RAIN, R.string.condition_drizzle_rain);
        STR_MAP.put(DRIZZLE_RAIN_HEAVY, R.string.condition_drizzle_rain_heavy);
        STR_MAP.put(DRIZZLE_SHOWER, R.string.condition_drizzle_shower);

        STR_MAP.put(RAIN_LIGHT, R.string.condition_rain_light);
        STR_MAP.put(RAIN, R.string.condition_rain);
        STR_MAP.put(RAIN_HEAVY, R.string.condition_rain_heavy);
        STR_MAP.put(RAIN_VERY_HEAVY, R.string.condition_rain_very_heavy);
        STR_MAP.put(RAIN_EXTREME, R.string.condition_rain_extreme);
        STR_MAP.put(RAIN_FREEZING, R.string.condition_rain_freezing);
        STR_MAP.put(RAIN_SHOWER_LIGHT, R.string.condition_rain_shower_light);
        STR_MAP.put(RAIN_SHOWER, R.string.condition_rain_shower);
        STR_MAP.put(RAIN_SHOWER_HEAVY, R.string.condition_rain_shower_heavy);

        STR_MAP.put(SNOW_LIGHT, R.string.condition_snow_light);
        STR_MAP.put(SNOW, R.string.condition_snow);
        STR_MAP.put(SNOW_HEAVY, R.string.condition_snow_heavy);
        STR_MAP.put(SLEET, R.string.condition_sleet);
        STR_MAP.put(SNOW_SHOWER, R.string.condition_snow_shower);

        STR_MAP.put(MIST, R.string.condition_mist);
        STR_MAP.put(SMOKE, R.string.condition_smoke);
        STR_MAP.put(HAZE, R.string.condition_haze);
        STR_MAP.put(SAND_WHIRLS, R.string.condition_sand_whirls);
        STR_MAP.put(FOG, R.string.condition_fog);

        STR_MAP.put(CLOUDS_CLEAR, R.string.condition_clouds_clear);
        STR_MAP.put(CLOUDS_FEW, R.string.condition_clouds_few);
        STR_MAP.put(CLOUDS_SCATTERED, R.string.condition_clouds_scattered);
        STR_MAP.put(CLOUDS_BROKEN, R.string.condition_clouds_broken);
        STR_MAP.put(CLOUDS_OVERCAST, R.string.condition_clouds_overcast);

        STR_MAP.put(TORNADO, R.string.condition_tornado);
        STR_MAP.put(TROPICAL_STORM, R.string.condition_tropical_storm);
        STR_MAP.put(HURRICANE, R.string.condition_hurricane);
        STR_MAP.put(COLD, R.string.condition_cold);
        STR_MAP.put(HOT, R.string.condition_hot);
        STR_MAP.put(WINDY, R.string.condition_windy);
        STR_MAP.put(HAIL, R.string.condition_hail);
    }

    static Map<WeatherConditionType, Integer> IMG_MAP = new EnumMap<WeatherConditionType, Integer>(WeatherConditionType.class);
    static {
        IMG_MAP.put(THUNDERSTORM_RAIN_LIGHT, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_RAIN, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_RAIN_HEAVY, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_LIGHT, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_HEAVY, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_RAGGED, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_DRIZZLE_LIGHT, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_DRIZZLE, R.drawable.condition_storm);
        IMG_MAP.put(THUNDERSTORM_DRIZZLE_HEAVY, R.drawable.condition_storm);

        IMG_MAP.put(DRIZZLE_LIGHT, R.drawable.condition_rain);
        IMG_MAP.put(DRIZZLE, R.drawable.condition_rain);
        IMG_MAP.put(DRIZZLE_HEAVY, R.drawable.condition_rain);
        IMG_MAP.put(DRIZZLE_RAIN_LIGHT, R.drawable.condition_rain);
        IMG_MAP.put(DRIZZLE_RAIN, R.drawable.condition_rain);
        IMG_MAP.put(DRIZZLE_RAIN_HEAVY, R.drawable.condition_rain);
        IMG_MAP.put(DRIZZLE_SHOWER, R.drawable.condition_shower);

        IMG_MAP.put(RAIN_LIGHT, R.drawable.condition_rain);
        IMG_MAP.put(RAIN, R.drawable.condition_rain);
        IMG_MAP.put(RAIN_HEAVY, R.drawable.condition_rain);
        IMG_MAP.put(RAIN_VERY_HEAVY, R.drawable.condition_rain);
        IMG_MAP.put(RAIN_EXTREME, R.drawable.condition_rain);
        IMG_MAP.put(RAIN_FREEZING, R.drawable.condition_rain);
        IMG_MAP.put(RAIN_SHOWER_LIGHT, R.drawable.condition_shower);
        IMG_MAP.put(RAIN_SHOWER, R.drawable.condition_shower);
        IMG_MAP.put(RAIN_SHOWER_HEAVY, R.drawable.condition_shower);

        IMG_MAP.put(SNOW_LIGHT, R.drawable.condition_snow);
        IMG_MAP.put(SNOW, R.drawable.condition_snow);
        IMG_MAP.put(SNOW_HEAVY, R.drawable.condition_snow);
        IMG_MAP.put(SLEET, R.drawable.condition_snow);
        IMG_MAP.put(SNOW_SHOWER, R.drawable.condition_snow);

        IMG_MAP.put(MIST, R.drawable.condition_mist);
        IMG_MAP.put(SMOKE, R.drawable.condition_mist);
        IMG_MAP.put(HAZE, R.drawable.condition_mist);
        IMG_MAP.put(SAND_WHIRLS, R.drawable.condition_mist);
        IMG_MAP.put(FOG, R.drawable.condition_mist);

        IMG_MAP.put(CLOUDS_CLEAR, R.drawable.condition_clear);
        IMG_MAP.put(CLOUDS_FEW, R.drawable.condition_clouds);
        IMG_MAP.put(CLOUDS_SCATTERED, R.drawable.condition_clouds);
        IMG_MAP.put(CLOUDS_BROKEN, R.drawable.condition_overcast);
        IMG_MAP.put(CLOUDS_OVERCAST, R.drawable.condition_overcast);

        IMG_MAP.put(TORNADO, R.drawable.condition_alert);
        IMG_MAP.put(TROPICAL_STORM, R.drawable.condition_alert);
        IMG_MAP.put(HURRICANE, R.drawable.condition_alert);
        IMG_MAP.put(COLD, R.drawable.condition_alert);
        IMG_MAP.put(HOT, R.drawable.condition_alert);
        IMG_MAP.put(WINDY, R.drawable.condition_alert);
        IMG_MAP.put(HAIL, R.drawable.condition_alert);
    }

    Context context;

    public WeatherConditionFormat(Context context) {
        this.context = context;
    }

    public String getText(WeatherCondition condition) {
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

    /**
     *  Returns the LevelListDrawable, suitable for this weather condition.
     *  The level of the drawable defines the desired drawable size in dp.
     */
    public LevelListDrawable getDrawable(WeatherCondition condition) {
        return (LevelListDrawable)this.context.getResources().getDrawable(getDrawableId(condition));
    }

    /**
     *  Returns the ID of the drawable icon, suitable for this weather condition.
     *  It's the LevelListDrawable, where the level defines the desired drawable size in dp.
     */
    public static int getDrawableId(WeatherCondition condition) {
        Collection<WeatherConditionType> types = condition.getConditionTypes();
        if (types == null || types.isEmpty()) {
            return getDrawableId(WeatherConditionType.CLOUDS_CLEAR);
        }
        return getDrawableId(types.iterator().next());
    }

    static Integer getStringId(WeatherConditionType type) {
        return STR_MAP.get(type);
    }

    static Integer getDrawableId(WeatherConditionType type) {
        return IMG_MAP.get(type);
    }

}
