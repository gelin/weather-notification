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
import java.util.Collection;
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
            conditionEditor.putOrRemove(CONDITION_TYPES, condition.getConditionTypes());
            
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
            conditionEditor.remove(CONDITION_TYPES, 0);

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
            ConditionPreferencesReader conditionReader = new ConditionPreferencesReader(preferences, i);
            
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText(conditionReader.get(CONDITION_TEXT, ""));

            Collection<WeatherConditionType> types = conditionReader.getEnums(CONDITION_TYPES, WeatherConditionType.class);
            for (WeatherConditionType type : types) {
                condition.addConditionType(type);
            }
            
            TemperatureUnit tunit = conditionReader.get(TEMPERATURE_UNIT, TemperatureUnit.F);
            SimpleTemperature temp = new SimpleTemperature(tunit);
            temp.setCurrent(conditionReader.get(CURRENT_TEMP, Temperature.UNKNOWN), tunit);
            temp.setLow(conditionReader.get(LOW_TEMP, Temperature.UNKNOWN), tunit);
            temp.setHigh(conditionReader.get(HIGH_TEMP, Temperature.UNKNOWN), tunit);
            condition.setTemperature(temp);
            
            SimpleHumidity hum = new SimpleHumidity();
            hum.setValue(conditionReader.get(HUMIDITY_VALUE, Humidity.UNKNOWN));
            hum.setText(conditionReader.get(HUMIDITY_TEXT, ""));
            condition.setHumidity(hum);
            
            WindSpeedUnit windSpeedUnit = conditionReader.get(WIND_SPEED_UNIT, WindSpeedUnit.MPH);
            SimpleWind wind = new SimpleWind(windSpeedUnit);
            wind.setSpeed(conditionReader.get(WIND_SPEED, Wind.UNKNOWN), windSpeedUnit);
            wind.setDirection(conditionReader.get(WIND_DIR, WindDirection.N));
            wind.setText(conditionReader.get(WIND_TEXT, ""));
            condition.setWind(wind);

            CloudinessUnit cloudinessUnit = conditionReader.get(CLOUDINESS_UNIT, CloudinessUnit.OKTA);
            SimpleCloudiness cloudiness = new SimpleCloudiness(cloudinessUnit);
            cloudiness.setValue(conditionReader.get(CLOUDINESS_VALUE, Cloudiness.UNKNOWN), cloudinessUnit);
            condition.setCloudiness(cloudiness);

            PrecipitationUnit precipitationUnit = conditionReader.get(PRECIPITATION_UNIT, PrecipitationUnit.MM);
            SimplePrecipitation precipitation = new SimplePrecipitation(precipitationUnit);
            PrecipitationPeriod precipitationPeriod = PrecipitationPeriod.valueOf(
                    conditionReader.get(PRECIPITATION_PERIOD, PrecipitationPeriod.PERIOD_1H.getHours()));
            precipitation.setValue(conditionReader.get(PRECIPITATION_VALUE, Precipitation.UNKNOWN), precipitationPeriod);
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

    private abstract static class ConditionPreferences {

        int index;

        protected ConditionPreferences(int index) {
            this.index = index;
        }

        protected String formatKey(String keyTemplate) {
            return String.format(keyTemplate, this.index);
        }

        protected String formatKey(String keyTemplate, int subindex) {
            return String.format(keyTemplate, this.index, subindex);
        }

    }

    private static class ConditionPreferencesEditor extends ConditionPreferences {

        SharedPreferences.Editor editor;

        public ConditionPreferencesEditor(SharedPreferences.Editor editor, int index) {
            super(index);
            this.editor = editor;
        }

        public void remove(String key) {
            this.editor.remove(formatKey(key));
        }

        public void remove(String key, int subindex) {
            this.editor.remove(formatKey(key, subindex));
        }

        public void putOrRemove(String key, String value) {
            if (value == null || "".equals(value)) {
                this.editor.remove(formatKey(key));
            } else {
                this.editor.putString(formatKey(key), value);
            }
        }

        public void putOrRemove(String key, Enum value) {
            if (value == null) {
                this.editor.remove(formatKey(key));
            } else {
                this.editor.putString(formatKey(key), String.valueOf(value));
            }
        }

        public void putOrRemove(String key, Collection values) {
            int i = 0;
            if (values != null) {
                for (Object value : values) {
                    this.editor.putString(formatKey(key, i), String.valueOf(value));
                    i++;
                }
            }
            this.editor.remove(formatKey(key, i));
        }

        public void putOrRemove(String key, int value) {
            if (value == Integer.MIN_VALUE) {
                this.editor.remove(formatKey(key));
            } else {
                this.editor.putInt(formatKey(key), value);
            }
        }

        public void putOrRemove(String key, float value) {
            if (value == Float.MIN_VALUE) {
                this.editor.remove(formatKey(key));
            } else {
                this.editor.putFloat(formatKey(key), value);
            }
        }

    }

    private static class ConditionPreferencesReader extends ConditionPreferences {

        SharedPreferences preferences;

        public ConditionPreferencesReader(SharedPreferences preferences, int index) {
            super(index);
            this.preferences = preferences;
        }

        public String get(String key, String defaultValue) {
            return this.preferences.getString(formatKey(key), defaultValue);
        }

        public <E extends Enum<E>> E get(String key, E defaultValue) {
            Class<? extends Enum> enumClass = defaultValue.getClass();
            return (E)Enum.valueOf(enumClass, this.preferences.getString(formatKey(key), String.valueOf(defaultValue)));
        }

        public <E extends Enum<E>> Collection<E> getEnums(String key, Class<E> enumClass) {
            List<E> result = new ArrayList<E>();
            int i = 0;
            while (this.preferences.contains(formatKey(key, i))) {
                result.add(Enum.valueOf(enumClass, this.preferences.getString(formatKey(key, i), null)));
                i++;
            }
            return result;
        }

        public int get(String key, int defaultValue) {
            return this.preferences.getInt(formatKey(key), defaultValue);
        }

        public float get(String key, float defaultValue) {
            return this.preferences.getFloat(formatKey(key), defaultValue);
        }

    }

}
