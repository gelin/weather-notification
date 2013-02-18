/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import ru.gelin.android.weather.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.gelin.android.weather.notification.WeatherStorageKeys.*;

/**
 *  Stores and retrieves the weather objects to SharedPreferences.
 *  The exact classes of the retrieved Weather can differ from the classes
 *  of the saved weather.
 */
@SuppressWarnings("deprecation")
public class WeatherStorage {
    
    /** Preference name for weather (this fake preference is updated 
     *  to call preference change listeners) */
    static final String WEATHER = "weather";
    
    /** SharedPreferences to store weather. */
    SharedPreferences preferences;
    
    /**
     *  Creates the storage for context.
     */
    public WeatherStorage(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //Log.d(TAG, "storage for " + context.getPackageName());
    }
    
    /**
     *  Saves the weather.
     */
    public void save(Weather weather) {
        Editor editor = preferences.edit();

        editor.putLong(WEATHER, System.currentTimeMillis());   //just current time
        editor.putString(LOCATION, weather.getLocation().getText());
        editor.putLong(TIME, weather.getTime().getTime());
        editor.putLong(QUERY_TIME, weather.getQueryTime().getTime());

        if (weather.getForecastURL() == null) {
            editor.remove(FORECAST_URL);
        } else {
            editor.putString(FORECAST_URL, String.valueOf(weather.getForecastURL()));
        }

        if (weather.getUnitSystem() == null) {
            editor.remove(UNIT_SYSTEM);
        } else {
            editor.putString(UNIT_SYSTEM, weather.getUnitSystem().toString());
        }

        int i = 0;
        for (WeatherCondition condition : weather.getConditions()) {
            ConditionPreferencesEditor conditionEditor =
                    new ConditionPreferencesEditor(editor, i);
            
            conditionEditor.putOrRemove(CONDITION_TEXT, condition.getConditionText());
            
            Temperature temp = condition.getTemperature();
            conditionEditor.putOrRemove(TEMPERATURE_UNIT, temp.getTemperatureUnit());
            conditionEditor.putOrRemove(CURRENT_TEMP, temp.getCurrent());
            conditionEditor.putOrRemove(LOW_TEMP, temp.getLow());
            conditionEditor.putOrRemove(HIGH_TEMP, temp.getHigh());

            Humidity hum = condition.getHumidity();
            conditionEditor.putOrRemove(HUMIDITY_VALUE, hum.getValue());
            conditionEditor.putOrRemove(HUMIDITY_TEXT, hum.getText());
            
            Wind wind = condition.getWind();
            conditionEditor.putOrRemove(WIND_SPEED_UNIT, wind.getSpeedUnit());
            conditionEditor.putOrRemove(WIND_SPEED, wind.getSpeed());
            conditionEditor.putOrRemove(WIND_DIR, wind.getDirection());
            conditionEditor.putOrRemove(WIND_TEXT, wind.getText());

            Cloudiness cloudiness = condition.getCloudiness();
            if (cloudiness == null) {
                conditionEditor.remove(CLOUDINESS_UNIT);
                conditionEditor.remove(CLOUDINESS_VALUE);
            } else {
                conditionEditor.putOrRemove(CLOUDINESS_UNIT, cloudiness.getCloudinessUnit());
                conditionEditor.putOrRemove(CLOUDINESS_VALUE, cloudiness.getValue());
            }

            Precipitation precipitation = condition.getPrecipitation();
            if (precipitation == null) {
                conditionEditor.remove(PRECIPITATION_UNIT);
                conditionEditor.remove(PRECIPITATION_PERIOD);
                conditionEditor.remove(PRECIPITATION_VALUE);
            } else {
                conditionEditor.putOrRemove(PRECIPITATION_UNIT, precipitation.getPrecipitationUnit());
                conditionEditor.putOrRemove(PRECIPITATION_PERIOD, PrecipitationPeriod.PERIOD_1H.getHours());
                conditionEditor.putOrRemove(PRECIPITATION_VALUE, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
            }

            i++;
        }

        for (; i < 4; i++) {
            ConditionPreferencesEditor conditionEditor =
                    new ConditionPreferencesEditor(editor, i);

            conditionEditor.remove(CONDITION_TEXT);

            conditionEditor.remove(TEMPERATURE_UNIT);
            conditionEditor.remove(CURRENT_TEMP);
            conditionEditor.remove(LOW_TEMP);
            conditionEditor.remove(HIGH_TEMP);
            conditionEditor.remove(HUMIDITY_VALUE);
            conditionEditor.remove(HUMIDITY_TEXT);

            conditionEditor.remove(WIND_SPEED_UNIT);
            conditionEditor.remove(WIND_SPEED);
            conditionEditor.remove(WIND_DIR);
            conditionEditor.remove(WIND_TEXT);

            conditionEditor.remove(CLOUDINESS_UNIT);
            conditionEditor.remove(CLOUDINESS_VALUE);

            conditionEditor.remove(PRECIPITATION_UNIT);
            conditionEditor.remove(PRECIPITATION_PERIOD);
            conditionEditor.remove(PRECIPITATION_VALUE);
        }

        editor.commit();
    }
    
    /**
     *  Loads the weather.
     *  The values of the saved weather are restored, not exact classes.
     */
    public Weather load() {
        SimpleWeather weather = new ParcelableWeather2();
        Location location = new SimpleLocation(
                preferences.getString(LOCATION, ""));
        weather.setLocation(location);
        weather.setTime(new Date(preferences.getLong(TIME, 0)));
        weather.setQueryTime(new Date(preferences.getLong(QUERY_TIME, System.currentTimeMillis())));
        try {
            weather.setForecastURL(new URL(preferences.getString(FORECAST_URL, "")));
        } catch (MalformedURLException e) {
            weather.setForecastURL(null);
        }

        int i = 0;
        List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
        while (preferences.contains(String.format(CONDITION_TEXT, i))) {
            
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText(preferences.getString(
                    String.format(CONDITION_TEXT, i), ""));
            
            TemperatureUnit tunit = TemperatureUnit.valueOf(
                    preferences.getString(String.format(TEMPERATURE_UNIT, i), 
                            TemperatureUnit.F.toString()));
            SimpleTemperature temp = new SimpleTemperature(tunit);
            temp.setCurrent(preferences.getInt(String.format(CURRENT_TEMP, i),
                    Temperature.UNKNOWN), tunit);
            temp.setLow(preferences.getInt(String.format(LOW_TEMP, i),
                    Temperature.UNKNOWN), tunit);
            temp.setHigh(preferences.getInt(String.format(HIGH_TEMP, i),
                    Temperature.UNKNOWN), tunit);
            condition.setTemperature(temp);
            
            SimpleHumidity hum = new SimpleHumidity();
            hum.setValue(preferences.getInt(String.format(HUMIDITY_VALUE, i), Humidity.UNKNOWN));
            hum.setText(preferences.getString(String.format(HUMIDITY_TEXT, i), ""));
            condition.setHumidity(hum);
            
            WindSpeedUnit windSpeedUnit = WindSpeedUnit.valueOf(
                    preferences.getString(String.format(WIND_SPEED_UNIT, i), 
                            WindSpeedUnit.MPH.toString()));
            SimpleWind wind = new SimpleWind(windSpeedUnit);
            wind.setSpeed(preferences.getInt(
                    String.format(WIND_SPEED, i), Wind.UNKNOWN), windSpeedUnit);
            wind.setDirection(WindDirection.valueOf(preferences.getString(
                    String.format(WIND_DIR, i), WindDirection.N.toString())));
            wind.setText(preferences.getString(String.format(WIND_TEXT, i), ""));
            condition.setWind(wind);

            CloudinessUnit cloudinessUnit = CloudinessUnit.valueOf(
                    preferences.getString(String.format(CLOUDINESS_UNIT, i),
                            CloudinessUnit.OKTA.toString()));
            SimpleCloudiness cloudiness = new SimpleCloudiness(cloudinessUnit);
            cloudiness.setValue(preferences.getInt(
                    String.format(CLOUDINESS_VALUE, i), Cloudiness.UNKNOWN), cloudinessUnit);
            condition.setCloudiness(cloudiness);

            PrecipitationUnit precipitationUnit = PrecipitationUnit.valueOf(
                    preferences.getString(String.format(PRECIPITATION_UNIT, i),
                            PrecipitationUnit.MM.toString()));
            SimplePrecipitation precipitation = new SimplePrecipitation(precipitationUnit);
            PrecipitationPeriod precipitationPeriod = PrecipitationPeriod.valueOf(
                    preferences.getInt(String.format(PRECIPITATION_PERIOD, i),
                            PrecipitationPeriod.PERIOD_1H.getHours()));
            precipitation.setValue(preferences.getFloat(
                    String.format(PRECIPITATION_VALUE, i), Precipitation.UNKNOWN), precipitationPeriod);
            condition.setPrecipitation(precipitation);

            conditions.add(condition);
            i++;
        }
        weather.setConditions(conditions);
        return weather;
    }
    
    /**
     *  Updates just "weather" preference to signal that the weather update is performed,
     *  but nothing is changed.
     */
    public void updateTime() {
        Editor editor = preferences.edit();
        editor.putLong(WEATHER, System.currentTimeMillis());   //just current time
        editor.commit();
    }

    private static class ConditionPreferencesEditor {

        SharedPreferences.Editor editor;
        int index;

        public ConditionPreferencesEditor(SharedPreferences.Editor editor, int index) {
            this.editor = editor;
            this.index = index;
        }

        private String formatKey(String keyTemplate) {
            return String.format(keyTemplate, this.index);
        }

        public void remove(String key) {
            this.editor.remove(formatKey(key));
        }

        public void putOrRemove(String key, String value) {
            if (value == null || "".equals(value)) {
                this.editor.remove(formatKey(key));
            } else {
                editor.putString(formatKey(key), value);
            }
        }

        public void putOrRemove(String key, Enum value) {
            if (value == null) {
                this.editor.remove(formatKey(key));
            } else {
                editor.putString(formatKey(key), String.valueOf(value));
            }
        }

        public void putOrRemove(String key, int value) {
            if (value == Integer.MIN_VALUE) {
                editor.remove(formatKey(key));
            } else {
                editor.putInt(formatKey(key), value);
            }
        }

        public void putOrRemove(String key, float value) {
            if (value == Float.MIN_VALUE) {
                editor.remove(formatKey(key));
            } else {
                editor.putFloat(formatKey(key), value);
            }
        }

    }

}
