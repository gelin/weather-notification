package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.*;

public class WeatherConditionFormatTest extends AndroidTestCase {

    WeatherConditionFormat format;

    public void setUp() {
        format = new WeatherConditionFormat(getContext());
    }

    public void testGetStringIdPrec() {
        testConditionType(R.string.condition_extreme_prec, 50.1f);
        testConditionType(R.string.condition_heavy_prec, 4.1f);
        testConditionType(R.string.condition_moderate_prec, 1.1f);
    }

    private void testConditionType(int expected, float prec) {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.setValue(prec, PrecipitationPeriod.PERIOD_1H);
        condition.setPrecipitation(precipitation);
        assertEquals(expected, format.getStringId(condition));
    }

    public void testGetConditionTypeLightPrec() {
        testConditionType(R.string.condition_skc_prec, 0.2f, 0);
        testConditionType(R.string.condition_few_prec, 0.2f, 1);
        testConditionType(R.string.condition_sct_prec, 0.2f, 3);
        testConditionType(R.string.condition_bkn_prec, 0.2f, 5);
        testConditionType(R.string.condition_ovc_prec, 0.2f, 8);
    }

    public void testGetConditionTypeNoPrec() {
        testConditionType(R.string.condition_skc, 0.0f, 0);
        testConditionType(R.string.condition_few, 0.0f, 1);
        testConditionType(R.string.condition_sct, 0.0f, 3);
        testConditionType(R.string.condition_bkn, 0.0f, 5);
        testConditionType(R.string.condition_ovc, 0.0f, 8);
    }

    private void testConditionType(int expected, float prec, int okta) {
        OpenWeatherMapWeatherCondition condition = new OpenWeatherMapWeatherCondition();
        SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.setValue(prec, PrecipitationPeriod.PERIOD_1H);
        condition.setPrecipitation(precipitation);
        SimpleCloudiness cloudiness = new SimpleCloudiness(CloudinessUnit.OKTA);
        cloudiness.setValue(okta, CloudinessUnit.OKTA);
        condition.setCloudiness(cloudiness);
        assertEquals(expected, format.getStringId(condition));
    }

}
