package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.*;

public class OpenWeatherMapWeatherConditionTest extends AndroidTestCase {

    public void testConditionTypePriority() {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        condition.addConditionType(WeatherConditionType.CLOUDS_BROKEN);
        condition.addConditionType(WeatherConditionType.RAIN);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    public void testConditionTypeStrength() {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        condition.addConditionType(WeatherConditionType.RAIN);
        condition.addConditionType(WeatherConditionType.RAIN_EXTREME);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN_EXTREME));
        assertFalse(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    public void testConditionTypeStrengthAndPriority() {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        condition.addConditionType(WeatherConditionType.CLOUDS_BROKEN);
        condition.addConditionType(WeatherConditionType.RAIN_EXTREME);
        condition.addConditionType(WeatherConditionType.RAIN);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.CLOUDS_BROKEN));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.RAIN_EXTREME));
        assertFalse(condition.getConditionTypes().contains(WeatherConditionType.RAIN));
    }

    public void testConditionTypeSameStrength() {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        condition.addConditionType(WeatherConditionType.TORNADO);
        condition.addConditionType(WeatherConditionType.HURRICANE);
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.TORNADO));
        assertTrue(condition.getConditionTypes().contains(WeatherConditionType.HURRICANE));
    }

}
