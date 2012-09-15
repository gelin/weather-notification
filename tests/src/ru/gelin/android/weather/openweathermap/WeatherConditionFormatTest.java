package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.*;

public class WeatherConditionFormatTest extends AndroidTestCase {

    WeatherConditionFormat format;

    public void setUp() {
        format = new WeatherConditionFormat(getContext());
    }

    public void testGetStringId() {
        assertEquals(R.string.condition_rain_light, (int)WeatherConditionFormat.getStringId(WeatherConditionType.RAIN_LIGHT));
        assertEquals(R.string.condition_tornado, (int)WeatherConditionFormat.getStringId(WeatherConditionType.TORNADO));
    }

    public void testGetText() {
        testConditionText("Rain", new WeatherConditionType[]{WeatherConditionType.RAIN, WeatherConditionType.CLOUDS_BROKEN});
        testConditionText("Tornado", new WeatherConditionType[]{WeatherConditionType.RAIN, WeatherConditionType.TORNADO});
        testConditionText("Tornado, Hurricane", new WeatherConditionType[]{WeatherConditionType.TORNADO, WeatherConditionType.HURRICANE});
    }

    private void testConditionText(String expected, WeatherConditionType[] types) {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        for (WeatherConditionType type : types) {
            condition.addConditionType(type);
        }
        WeatherConditionFormat format = new WeatherConditionFormat(getContext());
        assertEquals(expected, format.getText(condition));
    }

    public void testEmptyCondition() {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        WeatherConditionFormat format = new WeatherConditionFormat(getContext());
        assertEquals("Sky is clear", format.getText(condition));
    }

}
