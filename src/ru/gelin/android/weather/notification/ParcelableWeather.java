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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.gelin.android.weather.Humidity;
import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleHumidity;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.SimpleWeather;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.SimpleWind;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableWeather extends SimpleWeather implements Parcelable {

    public ParcelableWeather() {
    }
    
    /**
     *  Copy constructor.
     */
    public ParcelableWeather(Weather weather) {
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
        
        TemperatureUnit unit = weather.getTemperatureUnit();
        if (unit == null) {
            unit = TemperatureUnit.C;
        }
        setTemperatureUnit(unit);
        
        WindSpeedUnit wsunit = weather.getWindSpeedUnit();
        if (wsunit == null) {
            wsunit = WindSpeedUnit.MPH;
        }
        setWindSpeedUnit(wsunit);
        List<WeatherCondition> conditions = weather.getConditions();
        if (conditions == null) {
            return;
        }
        List<WeatherCondition> copyConditions = new ArrayList<WeatherCondition>();
        for (WeatherCondition condition : conditions) {
            SimpleWeatherCondition copyCondition = new SimpleWeatherCondition();
            copyCondition.setConditionText(condition.getConditionText());
            Temperature temp = condition.getTemperature(unit);
            SimpleTemperature copyTemp = new SimpleTemperature(unit);
            if (temp != null) {
                copyTemp.setCurrent(temp.getCurrent(), unit);
                copyTemp.setLow(temp.getLow(), unit);
                copyTemp.setHigh(temp.getHigh(), unit);
            }
            copyCondition.setTemperature(copyTemp);
            
            Humidity hum = condition.getHumidity();
            SimpleHumidity copyHum = new SimpleHumidity();
            if (hum != null) {
                copyHum.setValue(hum.getValue());
                copyHum.setText(hum.getText());
            }
            copyCondition.setHumidity(copyHum);
            
            Wind wind = condition.getWind(wsunit);
            SimpleWind copyWind = new SimpleWind(wsunit);
            if (wind != null) {
                copyWind.setSpeed(wind.getSpeed(), wsunit);
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
        Location location = getLocation();
        if (location == null) {
            dest.writeString(null);
        } else {
            dest.writeString(location.getText());
        }
        Date time = getTime();
        if (time == null) {
            dest.writeLong(0);
        } else {
            dest.writeLong(time.getTime());
        }
        TemperatureUnit unit = getTemperatureUnit();
        if (unit == null) {
            dest.writeString(null);
        } else {
            dest.writeString(unit.toString());
        }
        WindSpeedUnit wsunit = getWindSpeedUnit();
        if (wsunit == null) {
            wsunit = WindSpeedUnit.MPH;
        }
        dest.writeString(wsunit.toString());
        int i = 0;
        if (getConditions() == null) {
            return;
        }
        for (WeatherCondition condition : getConditions()) {
            dest.writeString(condition.getConditionText());
            Temperature temp = condition.getTemperature(unit);
            if (temp == null) {
                continue;
            }
            dest.writeInt(temp.getCurrent());
            dest.writeInt(temp.getLow());
            dest.writeInt(temp.getHigh());
            dest.writeInt(condition.getHumidity().getValue());
            dest.writeString(condition.getHumidity().getText());
            dest.writeInt(condition.getWind(wsunit).getSpeed());
            dest.writeString(condition.getWind(wsunit).getDirection().toString());
            dest.writeString(condition.getWind(wsunit).getText());
            i++;
        }
    }
    
    private ParcelableWeather(Parcel in) {
        setLocation(new SimpleLocation(in.readString()));
        setTime(new Date(in.readLong()));
        String unit = in.readString();
        try {
            setTemperatureUnit(TemperatureUnit.valueOf(unit));
        } catch (Exception e) {
            setTemperatureUnit(TemperatureUnit.C);
        }
        String wsunit = in.readString();
        try {
            setWindSpeedUnit(WindSpeedUnit.valueOf(wsunit));
        } catch (Exception e) {
        	setWindSpeedUnit(WindSpeedUnit.MPH);
        }
        List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
        while (in.dataAvail() > 6) {    //each condition takes 6 positions
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText(in.readString());
            SimpleTemperature temp = new SimpleTemperature(getTemperatureUnit());
            temp.setCurrent(in.readInt(), getTemperatureUnit());
            temp.setLow(in.readInt(), getTemperatureUnit());
            temp.setHigh(in.readInt(), getTemperatureUnit());
            condition.setTemperature(temp);
            
            SimpleHumidity hum = new SimpleHumidity();
            hum.setValue(in.readInt());
            hum.setText(in.readString());
            condition.setHumidity(hum);
            
            SimpleWind wind = new SimpleWind(getWindSpeedUnit());
            wind.setSpeed(in.readInt(), getWindSpeedUnit());
            wind.setDirection(WindDirection.valueOf(in.readString()));
            wind.setText(in.readString());
            condition.setWind(wind);
            conditions.add(condition);
        }
        setConditions(conditions);
    }
    
    public static final Parcelable.Creator<ParcelableWeather> CREATOR = 
            new Parcelable.Creator<ParcelableWeather>() {
        public ParcelableWeather createFromParcel(Parcel in) {
            return new ParcelableWeather(in);
        }
        public ParcelableWeather[] newArray(int size) {
            return new ParcelableWeather[size];
        }
    };

}
