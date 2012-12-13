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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import ru.gelin.android.weather.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.gelin.android.weather.notification.ParcelableWeatherKeys.*;

public class ParcelableWeather2 extends SimpleWeather implements Parcelable {

    /** Parcelable representation version */
    static final int VERSION = 2;
    
    public ParcelableWeather2() {
    }

    /**
     *  Copy constructor.
     */
    public ParcelableWeather2(Weather weather) {
        Location location = weather.getLocation();
        if (location == null) {
            setLocation(new SimpleLocation(null));
        } else {
            setLocation(new SimpleLocation(location.getText()));
        }
        Date time = weather.getTime();
        if (time == null) {
            setTime(new Date(0));
        } else {
            setTime(time);
        }
        Date queryTime = weather.getQueryTime();
        if (time == null) {
            setQueryTime(new Date());
        } else {
            setQueryTime(queryTime);
        }
        URL forecastURL = weather.getForecastURL();
        setForecastURL(forecastURL);

        List<WeatherCondition> conditions = weather.getConditions();
        if (conditions == null) {
            return;
        }
        List<WeatherCondition> copyConditions = new ArrayList<WeatherCondition>();
        for (WeatherCondition condition : conditions) {
            SimpleWeatherCondition copyCondition = new SimpleWeatherCondition();
            copyCondition.setConditionText(condition.getConditionText());
            Temperature temp = condition.getTemperature();
            TemperatureUnit tempUnit = temp.getTemperatureUnit();
            SimpleTemperature copyTemp = new SimpleTemperature(tempUnit);
            if (temp != null) {
                copyTemp.setCurrent(temp.getCurrent(), tempUnit);
                copyTemp.setLow(temp.getLow(), tempUnit);
                copyTemp.setHigh(temp.getHigh(), tempUnit);
            }
            copyCondition.setTemperature(copyTemp);
            
            Humidity hum = condition.getHumidity();
            SimpleHumidity copyHum = new SimpleHumidity();
            if (hum != null) {
                copyHum.setValue(hum.getValue());
                copyHum.setText(hum.getText());
            }
            copyCondition.setHumidity(copyHum);
            
            Wind wind = condition.getWind();
            WindSpeedUnit windUnit = wind.getSpeedUnit();
            SimpleWind copyWind = new SimpleWind(windUnit);
            if (wind != null) {
                copyWind.setSpeed(wind.getSpeed(), windUnit);
                copyWind.setDirection(wind.getDirection());
                copyWind.setText(wind.getText());
            }
            copyCondition.setWind(copyWind);
            copyConditions.add(copyCondition);
        }
        setConditions(copyConditions);
    }
    
    //@Override
    public int describeContents() {
        return 0;
    }

    //@Override
    public void writeToParcel(Parcel dest, int flags) {
        writeVersion(dest);
        writeCommonParams(dest);
        for (WeatherCondition condition : getConditions()) {
            writeCondition(condition, dest);
        }
    }
    
    void writeVersion(Parcel dest) {
        dest.writeInt(VERSION);
    }
    
    void writeCommonParams(Parcel dest) {
        Bundle bundle = new Bundle();
        Location location = getLocation();
        if (location != null) {
            bundle.putString(LOCATION, location.getText());
        }
        Date time = getTime();
        if (time != null) {
            bundle.putLong(TIME, time.getTime());
        }
        Date queryTime = getQueryTime();
        if (queryTime != null) {
            bundle.putLong(QUERY_TIME, queryTime.getTime());
        }
        URL forecastURL = getForecastURL();
        if (forecastURL != null) {
            bundle.putString(FORECAST_URL, String.valueOf(forecastURL));
        }
        List<WeatherCondition> conditions = getConditions();
        if (conditions == null) {
            bundle.putInt(CONDITIONS_NUMBER, 0);
        } else {
            bundle.putInt(CONDITIONS_NUMBER, conditions.size());
        }
        dest.writeBundle(bundle);
    }
    
    void writeCondition(WeatherCondition condition, Parcel dest) {
        Bundle bundle = new Bundle();
        bundle.putString(CONDITION_TEXT, condition.getConditionText());
        writeTemperature(condition.getTemperature(), bundle);
        writeHumidity(condition.getHumidity(), bundle);
        writeWind(condition.getWind(), bundle);
        dest.writeBundle(bundle);
    }
    
    void writeTemperature(Temperature temp, Bundle bundle) {
        if (temp == null) {
            return;
        }
        bundle.putString(TEMPERATURE_UNIT, temp.getTemperatureUnit().toString());
        bundle.putInt(CURRENT_TEMP, temp.getCurrent());
        bundle.putInt(LOW_TEMP, temp.getLow());
        bundle.putInt(HIGH_TEMP, temp.getHigh());
    }
    
    void writeHumidity(Humidity humidity, Bundle bundle) {
        if (humidity == null) {
            return;
        }
        bundle.putString(HUMIDITY_TEXT, humidity.getText());
        bundle.putInt(HUMIDITY_VAL, humidity.getValue());
    }
    
    void writeWind(Wind wind, Bundle bundle) {
        if (wind == null) {
            return;
        }
        bundle.putString(WIND_TEXT, wind.getText());
        bundle.putString(WIND_SPEED_UNIT, wind.getSpeedUnit().toString());
        bundle.putInt(WIND_SPEED, wind.getSpeed());
        bundle.putString(WIND_DIR, wind.getDirection().toString());
    }
    
    private ParcelableWeather2(Parcel in) {
        int version = in.readInt();
        if (version != VERSION) {
            return;
        }
        int conditionsSize = readCommonParams(in);
        List<WeatherCondition> conditions = new ArrayList<WeatherCondition>(conditionsSize);
        for (int i = 0; i < conditionsSize; i++) {
            WeatherCondition condition = readCondition(in);
            if (condition != null) {
                conditions.add(condition);
            }
        }
        setConditions(conditions);
    }
    
    /**
     *  Reads and sets common weather params.
     *  @return number of conditions
     */
    int readCommonParams(Parcel in) {
        Bundle bundle = in.readBundle(this.getClass().getClassLoader());
        setLocation(new SimpleLocation(bundle.getString(LOCATION)));
        setTime(new Date(bundle.getLong(TIME)));
        setQueryTime(new Date(bundle.getLong(QUERY_TIME)));
        try {
            setForecastURL(new URL(bundle.getString(FORECAST_URL)));
        } catch (MalformedURLException e) {
            setForecastURL(null);
        }
        return bundle.getInt(CONDITIONS_NUMBER);
    }
    
    WeatherCondition readCondition(Parcel in) {
        if (in.dataAvail() == 0) {
            return null;
        }
        Bundle bundle = in.readBundle(this.getClass().getClassLoader());
        SimpleWeatherCondition condition = new SimpleWeatherCondition();
        condition.setConditionText(bundle.getString(CONDITION_TEXT));
        
        condition.setTemperature(readTemperature(bundle));
        condition.setHumidity(readHumidity(bundle));
        condition.setWind(readWind(bundle));
        
        return condition;
    }
    
    SimpleTemperature readTemperature(Bundle bundle) {
        TemperatureUnit unit;
        try {
            unit = TemperatureUnit.valueOf(bundle.getString(TEMPERATURE_UNIT));
        } catch (Exception e) {
            return null;
        }
        SimpleTemperature temp = new SimpleTemperature(unit);
        temp.setCurrent(bundle.getInt(CURRENT_TEMP), unit);
        temp.setLow(bundle.getInt(LOW_TEMP), unit);
        temp.setHigh(bundle.getInt(HIGH_TEMP), unit);
        return temp;
    }
    
    SimpleHumidity readHumidity(Bundle bundle) {
        if (!bundle.containsKey(HUMIDITY_VAL)) {
            return null;
        }
        SimpleHumidity hum = new SimpleHumidity();
        hum.setValue(bundle.getInt(HUMIDITY_VAL));
        hum.setText(bundle.getString(HUMIDITY_TEXT));
        return hum;
    }
    
    SimpleWind readWind(Bundle bundle) {
        WindSpeedUnit unit;
        try {
            unit = WindSpeedUnit.valueOf(bundle.getString(WIND_SPEED_UNIT));
        } catch (Exception e) {
            return null;
        }
        SimpleWind wind = new SimpleWind(unit);
        wind.setSpeed(bundle.getInt(WIND_SPEED), unit);
        WindDirection dir;
        try {
            dir = WindDirection.valueOf(bundle.getString(WIND_DIR));
        } catch (Exception e) {
            return null;
        }
        wind.setDirection(dir);
        wind.setText(bundle.getString(WIND_TEXT));
        return wind;
    }
    
    public static final Parcelable.Creator<ParcelableWeather2> CREATOR =
            new Parcelable.Creator<ParcelableWeather2>() {
        public ParcelableWeather2 createFromParcel(Parcel in) {
            return new ParcelableWeather2(in);
        }
        public ParcelableWeather2[] newArray(int size) {
            return new ParcelableWeather2[size];
        }
    };

}
