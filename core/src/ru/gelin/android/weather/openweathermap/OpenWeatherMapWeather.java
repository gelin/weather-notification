package ru.gelin.android.weather.openweathermap;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.notification.skin.impl.WeatherConditionFormat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *  Weather implementation which constructs from the JSON received from openweathermap.org
 */
public class OpenWeatherMapWeather implements Weather {

    String FORECAST_URL_TEMPLATE="http://m.openweathermap.org/city/%d#forecast";

    /** City ID */
    int cityId = 0;
    /** Forecast URL */
    URL forecastURL;
    /** Weather location */
    SimpleLocation location = new SimpleLocation("");
    /** Weather time */
    Date time = new Date();
    /** Query time */
    Date queryTime = new Date();
    /** Weather conditions */
    List<SimpleWeatherCondition> conditions = new ArrayList<SimpleWeatherCondition>();
    /** Emptyness flag */
    boolean empty = true;
    /** Condition text format */
    WeatherConditionFormat conditionFormat;

    public OpenWeatherMapWeather(Context context) {
        this.conditionFormat = new WeatherConditionFormat(context);
    }

    public OpenWeatherMapWeather(Context context, JSONObject jsonObject) throws WeatherException {
        this(context);
        parseCurrentWeather(jsonObject);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Date getTime() {
        return new Date(this.time.getTime());
    }

    @Override
    public Date getQueryTime() {
        return new Date(this.queryTime.getTime());
    }

    @Override
    public UnitSystem getUnitSystem() {
        return UnitSystem.SI;
    }

    @Override
    public List<WeatherCondition> getConditions() {
        return Collections.unmodifiableList(new ArrayList<WeatherCondition>(this.conditions));
    }

    List<SimpleWeatherCondition> getOpenWeatherMapConditions() {
        return this.conditions;
    }

    @Override
    public boolean isEmpty() {
        return this.empty;
    }

    public int getCityId() {
        return this.cityId;
    }

    public URL getForecastURL() {
        return this.forecastURL;
    }

    void parseCurrentWeather(JSONObject json) throws WeatherException {
        try {
            int code = json.getInt("cod");
            if (code != 200) {
                this.empty = true;
                return;
            }
            parseCityId(json);
            parseLocation(json);
            parseTime(json);
            parseCondition(json);
        } catch (JSONException e) {
            throw new WeatherException("cannot parse the weather", e);
        }
        this.empty = false;
    }

    private void parseCityId(JSONObject weatherJSON) throws JSONException {
        this.cityId = weatherJSON.getInt("id");
        try {
            this.forecastURL = new URL(String.format(FORECAST_URL_TEMPLATE, this.cityId));
        } catch (MalformedURLException e) {
            this.forecastURL = null;
        }
    }

    private void parseLocation(JSONObject weatherJSON) {
        try {
            this.location = new SimpleLocation(weatherJSON.getString("name"), false);
        } catch (JSONException e) {
            this.location = new SimpleLocation("", false);
        }
    }

    private void parseTime(JSONObject weatherJSON) {
        try {
            long timestamp = weatherJSON.getLong("dt");
            this.time = new Date(timestamp * 1000);
        } catch (JSONException e) {
            this.time = new Date();
        }
    }

    private void parseCondition(JSONObject weatherJSON) {
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.setTemperature(parseTemperature(weatherJSON, TemperatureFields.CURRENT_WEATHER));
        condition.setWind(parseWind(weatherJSON));
        condition.setHumidity(parseHumidity(weatherJSON));
        condition.setPrecipitation(parsePrecipitation(weatherJSON));
        condition.setCloudiness(parseCloudiness(weatherJSON));
        parseWeatherType(weatherJSON, condition);
        condition.setConditionText(this.conditionFormat.getText(condition));
        this.conditions.add(condition);
    }

    static private enum TemperatureFields {
        CURRENT_WEATHER("main", "temp", "temp_min", "temp_max"),
        DAILY_FORECAST("temp", null, "min", "max");

        private String main;
        private String current;
        private String min;
        private String max;

        private TemperatureFields(String main, String current, String min, String max) {
            this.main = main;
            this.current = current;
            this.min = min;
            this.max = max;
        }
        public String getMain() {
            return this.main;
        }
        public String getCurrent() {
            return this.current;
        }
        public String getMin() {
            return this.min;
        }
        public String getMax() {
            return this.max;
        }
    }

    private SimpleTemperature parseTemperature(JSONObject weatherJSON, TemperatureFields fields) {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.K);
        JSONObject main;
        try {
            main = weatherJSON.getJSONObject(fields.getMain());
        } catch (JSONException e) {
            //temp is optional
            return temperature;
        }
        try {
            double currentTemp = main.getDouble(fields.getCurrent());
            temperature.setCurrent((int)currentTemp, TemperatureUnit.K);
        } catch (JSONException e) {
            //temp is optional
        }
        try {
            double minTemp = main.getDouble(fields.getMin());
            temperature.setLow((int)minTemp, TemperatureUnit.K);
        } catch (JSONException e) {
            //min temp is optional
        }
        try {
            double maxTemp = main.getDouble(fields.getMax());
            temperature.setHigh((int)maxTemp, TemperatureUnit.K);
        } catch (JSONException e) {
            //max temp is optional
        }
        return temperature;
    }

    private SimpleWind parseWind(JSONObject weatherJSON) {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPS);
        JSONObject windJSON;
        try {
            windJSON = weatherJSON.getJSONObject("wind");
        } catch (JSONException e) {
            //wind is optional
            return wind;
        }
        try {
            double speed = windJSON.getDouble("speed");
            wind.setSpeed((int)speed, WindSpeedUnit.MPS);
        } catch (JSONException e) {
            //wind speed is optional
        }
        try {
            double deg = windJSON.getDouble("deg");
            wind.setDirection(WindDirection.valueOf((int) deg));
        } catch (JSONException e) {
            //wind direction is optional
        }
        wind.setText(String.format("Wind: %s, %d m/s", String.valueOf(wind.getDirection()), wind.getSpeed()));
        return wind;
    }

    private SimpleHumidity parseHumidity(JSONObject weatherJSON) {
        SimpleHumidity humidity = new SimpleHumidity();
        try {
            double humidityValue = weatherJSON.getJSONObject("main").getDouble("humidity");
            humidity.setValue((int)humidityValue);
            humidity.setText(String.format("Humidity: %d%%", humidity.getValue()));
        } catch (JSONException e) {
            //humidity is optional
        }
        return humidity;
    }

    private AppendablePrecipitation parsePrecipitation(JSONObject weatherJSON) {
        AppendablePrecipitation precipitation = new AppendablePrecipitation(PrecipitationUnit.MM);
        try {
            precipitation.setValue((float)weatherJSON.getJSONObject("rain").getDouble("3h"), PrecipitationPeriod.PERIOD_3H);
        } catch (JSONException e) {
            //no rain
        }
        return precipitation;
    }

    private AppendablePrecipitation parseForecastPrecipitation(JSONObject weatherJSON) {
        AppendablePrecipitation precipitation = new AppendablePrecipitation(PrecipitationUnit.MM);
        try {
            precipitation.setValue((float)weatherJSON.getDouble("rain"), PrecipitationPeriod.PERIOD_3H);
        } catch (JSONException e) {
            //no rain
        }
        return precipitation;
    }

    private AppendableCloudiness parseCloudiness(JSONObject weatherJSON) {
        AppendableCloudiness cloudiness = new AppendableCloudiness(CloudinessUnit.PERCENT);
        try {
            cloudiness.setValue((int)weatherJSON.getJSONObject("clouds").getDouble("all"), CloudinessUnit.PERCENT);
        } catch (JSONException e) {
            //no clouds
        }
        return cloudiness;
    }

    private void parseWeatherType(JSONObject weatherJSON, SimpleWeatherCondition condition) {
        try {
            JSONArray weathers = weatherJSON.getJSONArray("weather");
            for (int i = 0; i < weathers.length(); i++) {
                JSONObject weather = weathers.getJSONObject(i);
                WeatherConditionType type = WeatherConditionTypeFactory.fromId(weather.getInt("id"));
                condition.addConditionType(type);
            }
        } catch (JSONException e) {
            //no weather type
        }
    }

    void parseDailyForecast(JSONObject json) throws WeatherException {
        try {
            JSONArray list = json.getJSONArray("list");
            int j = 0;
            SimpleWeatherCondition condition = getCondition(0);
            appendForecastTemperature(condition, list.getJSONObject(0));
            condition.setConditionText(this.conditionFormat.getText(condition));
            for (int i = 1; i < 4; i++) {
                condition = getCondition(i);
                appendForecast(condition, list.getJSONObject(i));
                condition.setConditionText(this.conditionFormat.getText(condition));
            }
        } catch (JSONException e) {
            throw new WeatherException("cannot parse forecasts", e);
        }
    }

    private SimpleWeatherCondition getCondition(int i) {
        while (i >= this.conditions.size()) {
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText("");
            condition.setTemperature(new AppendableTemperature(TemperatureUnit.K));
            condition.setHumidity(new SimpleHumidity());
            condition.setWind(new SimpleWind(WindSpeedUnit.MPS));
            condition.setPrecipitation(new AppendablePrecipitation(PrecipitationUnit.MM));
            condition.setCloudiness(new AppendableCloudiness(CloudinessUnit.PERCENT));
            this.conditions.add(condition);
        }
        return this.conditions.get(i);
    }

    private void appendForecastTemperature(SimpleWeatherCondition condition, JSONObject weatherJSON)
            throws JSONException {
        AppendableTemperature existedTemp = (AppendableTemperature)condition.getTemperature();
        SimpleTemperature newTemp = parseTemperature(weatherJSON, TemperatureFields.DAILY_FORECAST);
        existedTemp.append(newTemp);
    }

    private void appendForecast(SimpleWeatherCondition condition, JSONObject weatherJSON)
            throws JSONException {
        appendForecastTemperature(condition, weatherJSON);
        AppendablePrecipitation existedPrec = (AppendablePrecipitation)condition.getPrecipitation();
        SimplePrecipitation newPrec = parseForecastPrecipitation(weatherJSON);
        existedPrec.append(newPrec);
        AppendableCloudiness existedCloud = (AppendableCloudiness)condition.getCloudiness();
        SimpleCloudiness newCloud = parseCloudiness(weatherJSON);
        existedCloud.append(newCloud);
        parseWeatherType(weatherJSON, condition);
    }

}
