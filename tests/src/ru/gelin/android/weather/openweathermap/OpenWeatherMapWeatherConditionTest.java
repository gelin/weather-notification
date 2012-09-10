package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.*;

public class OpenWeatherMapWeatherConditionTest extends AndroidTestCase {

    public void testGetConditionTypePrec() {
        testConditionType(WeatherConditionType.EXTREME_PREC, 50.1f);
        testConditionType(WeatherConditionType.HEAVY_PREC, 4.1f);
        testConditionType(WeatherConditionType.MODERATE_PREC, 1.1f);
    }

    private void testConditionType(WeatherConditionType expected, float prec) {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.setValue(prec, PrecipitationPeriod.PERIOD_1H);
        condition.setPrecipitation(precipitation);
        assertEquals(expected, condition.getConditionType());
    }

    public void testGetConditionTypeLightPrec() {
        testConditionType(WeatherConditionType.SKC_PREC, 0.2f, 0);
        testConditionType(WeatherConditionType.FEW_PREC, 0.2f, 1);
        testConditionType(WeatherConditionType.SCT_PREC, 0.2f, 3);
        testConditionType(WeatherConditionType.BKN_PREC, 0.2f, 5);
        testConditionType(WeatherConditionType.OVC_PREC, 0.2f, 8);
    }

    public void testGetConditionTypeNoPrec() {
        testConditionType(WeatherConditionType.SKC, 0.0f, 0);
        testConditionType(WeatherConditionType.FEW, 0.0f, 1);
        testConditionType(WeatherConditionType.SCT, 0.0f, 3);
        testConditionType(WeatherConditionType.BKN, 0.0f, 5);
        testConditionType(WeatherConditionType.OVC, 0.0f, 8);
    }

    private void testConditionType(WeatherConditionType expected, float prec, int okta) {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.setValue(prec, PrecipitationPeriod.PERIOD_1H);
        condition.setPrecipitation(precipitation);
        SimpleCloudiness cloudiness = new SimpleCloudiness(CloudinessUnit.OKTA);
        cloudiness.setValue(okta, CloudinessUnit.OKTA);
        condition.setCloudiness(cloudiness);
        assertEquals(expected, condition.getConditionType());
    }

}
