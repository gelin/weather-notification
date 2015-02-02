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

    String FORECAST_URL_TEMPLATE="http://openweathermap.org/city/%d";

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
            WeatherParser parser = new CurrentWeatherParser(json);
            parser.parseCondition();
        } catch (JSONException e) {
            throw new WeatherException("cannot parse the weather", e);
        }
        this.empty = false;
    }

    void parseDailyForecast(JSONObject json) throws WeatherException {
        try {
            JSONArray list = json.getJSONArray("list");
            SimpleWeatherCondition condition = getCondition(0);
            if (list.length() > 0) {
                appendForecastTemperature(condition, list.getJSONObject(0));
                condition.setConditionText(this.conditionFormat.getText(condition));
            }
            for (int i = 1; i < 4 && i < list.length(); i++) {
                condition = getCondition(i);
                parseForecast(condition, list.getJSONObject(i));
                condition.setConditionText(this.conditionFormat.getText(condition));
            }
        } catch (JSONException e) {
            throw new WeatherException("cannot parse forecasts", e);
        }
    }

    private void parseCityId(JSONObject json) throws JSONException {
        this.cityId = json.getInt("id");
        try {
            this.forecastURL = new URL(String.format(FORECAST_URL_TEMPLATE, this.cityId));
        } catch (MalformedURLException e) {
            this.forecastURL = null;
        }
    }

    private void parseLocation(JSONObject json) {
        try {
            this.location = new SimpleLocation(json.getString("name"), false);
        } catch (JSONException e) {
            this.location = new SimpleLocation("", false);
        }
    }

    private void parseTime(JSONObject json) {
        try {
            long timestamp = json.getLong("dt");
            this.time = new Date(timestamp * 1000);
        } catch (JSONException e) {
            this.time = new Date();
        }
    }

    private abstract class WeatherParser {
        JSONObject json;
        WeatherParser(JSONObject json) {
            this.json = json;
        }

        public void parseCondition() {
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setTemperature(parseTemperature());
            condition.setWind(parseWind());
            condition.setHumidity(parseHumidity());
            condition.setPrecipitation(parsePrecipitation());
            condition.setCloudiness(parseCloudiness());
            parseWeatherType(condition);
            condition.setConditionText(OpenWeatherMapWeather.this.conditionFormat.getText(condition));
            OpenWeatherMapWeather.this.conditions.add(condition);
        }

        private SimpleTemperature parseTemperature() {
            AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.K);
            JSONObject main;
            if (!hasTemp()) {
                //temp is optional
                return temperature;
            }
            try {
                double currentTemp = getCurrentTemp();
                temperature.setCurrent((int)currentTemp, TemperatureUnit.K);
            } catch (JSONException e) {
                //temp is optional
            }
            try {
                double minTemp = getMinTemp();
                temperature.setLow((int)minTemp, TemperatureUnit.K);
            } catch (JSONException e) {
                //min temp is optional
            }
            try {
                double maxTemp = getMaxTemp();
                temperature.setHigh((int)maxTemp, TemperatureUnit.K);
            } catch (JSONException e) {
                //max temp is optional
            }
            return temperature;
        }

        protected abstract boolean hasTemp();
        protected abstract double getCurrentTemp() throws JSONException;
        protected abstract double getMinTemp() throws JSONException;
        protected abstract double getMaxTemp() throws JSONException;

        private SimpleWind parseWind() {
            SimpleWind wind = new SimpleWind(WindSpeedUnit.MPS);
            if (!hasWind()) {
                return wind;
            }
            try {
                double speed = getWindSpeed();
                wind.setSpeed((int)Math.round(speed), WindSpeedUnit.MPS);
            } catch (JSONException e) {
                //wind speed is optional
            }
            try {
                double deg = getWindDeg();
                wind.setDirection(WindDirection.valueOf((int) deg));
            } catch (JSONException e) {
                //wind direction is optional
            }
            wind.setText(String.format("Wind: %s, %d m/s", String.valueOf(wind.getDirection()), wind.getSpeed()));
            return wind;
        }

        protected abstract boolean hasWind();
        protected abstract double getWindSpeed() throws JSONException;
        protected abstract double getWindDeg() throws JSONException;

        private SimpleHumidity parseHumidity() {
            SimpleHumidity humidity = new SimpleHumidity();
            try {
                double humidityValue = getHumidity();
                humidity.setValue((int)humidityValue);
                humidity.setText(String.format("Humidity: %d%%", humidity.getValue()));
            } catch (JSONException e) {
                //humidity is optional
            }
            return humidity;
        }

        protected abstract double getHumidity() throws JSONException;

        private SimplePrecipitation parsePrecipitation() {
            SimplePrecipitation precipitation = new SimplePrecipitation(PrecipitationUnit.MM);
            try {
                precipitation.setValue(getPrecipitation(), PrecipitationPeriod.PERIOD_3H);
            } catch (JSONException e) {
                //no rain
            }
            return precipitation;
        }

        protected abstract float getPrecipitation() throws JSONException;

        public SimpleCloudiness parseCloudiness() {
            SimpleCloudiness cloudiness = new SimpleCloudiness(CloudinessUnit.PERCENT);
            try {
                cloudiness.setValue(getCloudiness(), CloudinessUnit.PERCENT);
            } catch (JSONException e) {
                //no clouds
            }
            return cloudiness;
        }

        protected abstract int getCloudiness() throws JSONException;

        public void parseWeatherType(SimpleWeatherCondition condition) {
            try {
                JSONArray weathers = this.json.getJSONArray("weather");
                for (int i = 0; i < weathers.length(); i++) {
                    JSONObject weather = weathers.getJSONObject(i);
                    WeatherConditionType type = WeatherConditionTypeFactory.fromId(weather.getInt("id"));
                    condition.addConditionType(type);
                }
            } catch (JSONException e) {
                //no weather type
            }
        }

    }

    private class CurrentWeatherParser extends WeatherParser {
        CurrentWeatherParser(JSONObject json) {
            super(json);
        }

        @Override
        protected boolean hasTemp() {
            try {
                this.json.getJSONObject("main");
                return true;
            } catch (JSONException e) {
                return false;
            }
        }

        @Override
        protected double getCurrentTemp() throws JSONException {
            return this.json.getJSONObject("main").getDouble("temp");
        }

        @Override
        protected double getMinTemp() throws JSONException {
            return this.json.getJSONObject("main").getDouble("temp_min");
        }

        @Override
        protected double getMaxTemp() throws JSONException {
            return this.json.getJSONObject("main").getDouble("temp_max");
        }

        @Override
        protected boolean hasWind() {
            try {
                this.json.getJSONObject("wind");
                return true;
            } catch (JSONException e) {
                return false;
            }
        }

        @Override
        protected double getWindSpeed() throws JSONException {
            return this.json.getJSONObject("wind").getDouble("speed");
        }

        @Override
        protected double getWindDeg() throws JSONException {
            return this.json.getJSONObject("wind").getDouble("deg");
        }

        @Override
        protected double getHumidity() throws JSONException {
            return this.json.getJSONObject("main").getDouble("humidity");
        }

        @Override
        protected float getPrecipitation() throws JSONException {
            return (float)this.json.getJSONObject("rain").getDouble("3h");
        }

        @Override
        protected int getCloudiness() throws JSONException {
            return (int)this.json.getJSONObject("clouds").getDouble("all");
        }

    }

    private class ForecastWeatherParser extends WeatherParser {
        ForecastWeatherParser(JSONObject json) {
            super(json);
        }

        @Override
        protected boolean hasTemp() {
            try {
                this.json.getJSONObject("temp");
                return true;
            } catch (JSONException e) {
                return false;
            }
        }

        @Override
        protected double getCurrentTemp() throws JSONException {
            throw new JSONException("no current temp in forecast");
        }

        @Override
        protected double getMinTemp() throws JSONException {
            return this.json.getJSONObject("temp").getDouble("min");
        }

        @Override
        protected double getMaxTemp() throws JSONException {
            return this.json.getJSONObject("temp").getDouble("max");
        }

        @Override
        protected boolean hasWind() {
            return true;   //considering forecasts always have wind
        }

        @Override
        protected double getWindSpeed() throws JSONException {
            return this.json.getDouble("speed");
        }

        @Override
        protected double getWindDeg() throws JSONException {
            return this.json.getDouble("deg");
        }

        @Override
        protected double getHumidity() throws JSONException {
            return this.json.getDouble("humidity");
        }

        @Override
        protected float getPrecipitation() throws JSONException {
            return (float)this.json.getDouble("rain");
        }

        @Override
        protected int getCloudiness() throws JSONException {
            return (int)this.json.getDouble("clouds");
        }

    }

    private SimpleWeatherCondition getCondition(int i) {
        while (i >= this.conditions.size()) {
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText("");
            condition.setTemperature(new AppendableTemperature(TemperatureUnit.K));
            condition.setHumidity(new SimpleHumidity());
            condition.setWind(new SimpleWind(WindSpeedUnit.MPS));
            condition.setPrecipitation(new SimplePrecipitation(PrecipitationUnit.MM));
            condition.setCloudiness(new SimpleCloudiness(CloudinessUnit.PERCENT));
            this.conditions.add(condition);
        }
        return this.conditions.get(i);
    }

    private void appendForecastTemperature(SimpleWeatherCondition condition, JSONObject json)
            throws JSONException {
        WeatherParser parser = new ForecastWeatherParser(json);
        AppendableTemperature existedTemp = (AppendableTemperature)condition.getTemperature();
        SimpleTemperature newTemp = parser.parseTemperature();
        existedTemp.append(newTemp);
    }

    private void parseForecast(SimpleWeatherCondition condition, JSONObject json)
            throws JSONException {
        appendForecastTemperature(condition, json);
        WeatherParser parser = new ForecastWeatherParser(json);
        condition.setHumidity(parser.parseHumidity());
        condition.setWind(parser.parseWind());
        condition.setPrecipitation(parser.parsePrecipitation());
        condition.setCloudiness(parser.parseCloudiness());
        parser.parseWeatherType(condition);
    }

}
