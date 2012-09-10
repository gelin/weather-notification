package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ru.gelin.android.weather.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class OpenWeatherMapWeatherTest extends AndroidTestCase {

    public void testNotEmpty() throws WeatherException, IOException, JSONException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        assertNotNull(weather);
        assertFalse(weather.isEmpty());
    }

    public void testNotNullLocation() {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext());
        assertNotNull(weather.getLocation());
        assertTrue(weather.isEmpty());
    }

    public void testGetTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(15, condition.getTemperature(TemperatureUnit.C).getCurrent());
    }

    public void testParseEmptyJSON() throws JSONException {
        JSONTokener parser = new JSONTokener("{}");
        try {
            new OpenWeatherMapWeather(getContext(), (JSONObject)parser.nextValue());
            fail();
        } catch (WeatherException e) {
            //passed
        }
    }

    public void testGetLocation() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        Location location = weather.getLocation();
        assertNotNull(location);
        assertEquals("Omsk", location.getText());
    }

    public void testGetTime() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        Date time = weather.getTime();
        assertNotNull(time);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(2012, Calendar.AUGUST, 28, 16, 00, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTime(), time);
    }

    public void testGetQueryTime() throws IOException, JSONException, WeatherException {
        long now = System.currentTimeMillis();
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        assertTrue(now < weather.getQueryTime().getTime());
    }

    public void testGetConditionText() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        String text = condition.getConditionText();
        assertEquals("Light precipitations, scattered clouds", text);
    }

    public void testGetLowTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(14, condition.getTemperature(TemperatureUnit.C).getLow());
    }

    public void testGetHighTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(16, condition.getTemperature(TemperatureUnit.C).getHigh());
    }

    public void testGetWind() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        Wind wind = condition.getWind(WindSpeedUnit.MPS);
        assertNotNull(wind);
        assertEquals(2, wind.getSpeed());
        assertEquals(WindDirection.N, wind.getDirection());
    }

    public void testGetHumidity() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        Humidity humidity = condition.getHumidity();
        assertNotNull(humidity);
        assertEquals(85, humidity.getValue());
    }

    public void testGetTemperatureUnit() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        WeatherCondition condition = weather.getConditions().get(0);
        assertEquals(TemperatureUnit.K, condition.getTemperature().getTemperatureUnit());
    }

    public void testGetEmptyWeather() throws JSONException, WeatherException {
        JSONTokener parser = new JSONTokener("{\"list\":[]}");
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), (JSONObject)parser.nextValue());
        assertTrue(weather.isEmpty());
    }

    public void testGetCityID() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        assertEquals(1496153, weather.getCityId());
    }

    public void testForecastsNulls() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        assertEquals(4, weather.getConditions().size());
        assertNotNull(weather.getConditions().get(3).getHumidity());
        assertNotNull(weather.getConditions().get(3).getWind());
    }

    public void testForecastGetLowTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals(284, conditions.get(0).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(281, conditions.get(1).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(283, conditions.get(2).getTemperature(TemperatureUnit.K).getLow());
        assertEquals(287, conditions.get(3).getTemperature(TemperatureUnit.K).getLow());
    }

    public void testForecastGetHighTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        assertEquals(290, conditions.get(0).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(295, conditions.get(1).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(298, conditions.get(2).getTemperature(TemperatureUnit.K).getHigh());
        assertEquals(299, conditions.get(3).getTemperature(TemperatureUnit.K).getHigh());
    }

    public void testForecastGetTemperature() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        //the current temp should come from the city JSON
        assertEquals(288, conditions.get(0).getTemperature(TemperatureUnit.K).getCurrent());
    }

    public void testForecastGetPrecipitations() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<OpenWeatherMapWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(4, conditions.size());
        assertEquals(1f, conditions.get(0).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H));  //current
        assertEquals(0.5f, conditions.get(1).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H));
        assertEquals(Precipitation.UNKNOWN, conditions.get(2).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H));
        assertEquals(Precipitation.UNKNOWN, conditions.get(3).getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H));
    }

    public void testForecastGetCloudiness() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<OpenWeatherMapWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(4, conditions.size());
        assertEquals(49, conditions.get(0).getCloudiness().getValue());  //current
        assertEquals(17, conditions.get(1).getCloudiness().getValue());
        assertEquals(42, conditions.get(2).getCloudiness().getValue());
        assertEquals(65, conditions.get(3).getCloudiness().getValue());
    }

    public void testForecastGetConditionType() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<OpenWeatherMapWeatherCondition> conditions = weather.getOpenWeatherMapConditions();
        assertEquals(4, conditions.size());
        assertEquals(WeatherConditionType.SCT_PREC, conditions.get(0).getConditionType());
        assertEquals(WeatherConditionType.FEW_PREC, conditions.get(1).getConditionType());
        assertEquals(WeatherConditionType.SCT, conditions.get(2).getConditionType());
        assertEquals(WeatherConditionType.BKN, conditions.get(3).getConditionType());
    }

    public void testForecastGetConditionText() throws IOException, JSONException, WeatherException {
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather(getContext(), readJSON("omsk_city.json"));
        weather.parseForecast(readJSON("omsk_forecast.json"));
        List<WeatherCondition> conditions = weather.getConditions();
        assertEquals(4, conditions.size());
        //TODO: implement weather conditions for forecasts
        assertEquals("Light precipitations, scattered clouds", conditions.get(0).getConditionText());
        assertEquals("Light precipitations, few clouds", conditions.get(1).getConditionText());
        assertEquals("Scattered clouds", conditions.get(2).getConditionText());
        assertEquals("Broken clouds", conditions.get(3).getConditionText());
    }

    JSONObject readJSON(String resourceName) throws IOException, JSONException {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream(resourceName));
        StringBuilder buffer = new StringBuilder();
        int c = reader.read();
        while (c >= 0) {
            buffer.append((char)c);
            c = reader.read();
        }
        JSONTokener parser = new JSONTokener(buffer.toString());
        return (JSONObject)parser.nextValue();
    }

}
