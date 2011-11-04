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
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.Wind;
import ru.gelin.android.weather.WindDirection;
import ru.gelin.android.weather.WindSpeedUnit;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("deprecation")
public class ParcelableWeather extends SimpleWeather implements Parcelable {

    public ParcelableWeather() {
    }

    /**
     *  Copy constructor.
     */
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
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
        if (getConditions() == null) {
            return;
        }
        try {
            WeatherCondition condition0 = getConditions().get(0);
            dest.writeString(condition0.getTemperature().getUnitSystem().toString());
        } catch (Exception e) {
            dest.writeString(UnitSystem.SI.toString());
        }
        for (WeatherCondition condition : getConditions()) {
            writeCondition(condition, dest);
        }
        //TODO: think how to leave the backward compatibility AND add new properties
        /*
        for (WeatherCondition condition : getConditions()) {
            writeMoreCondition(condition, dest);
        }
        */
    }
    
    void writeCondition(WeatherCondition condition, Parcel dest) {
        dest.writeString(condition.getConditionText());
        writeTemperature(condition.getTemperature(), dest);
        writeHumidity(condition.getHumidity(), dest);
        writeWind(condition.getWind(), dest);
    }
    
    void writeMoreCondition(WeatherCondition condition, Parcel dest) {
        writeMoreTemperature(condition.getTemperature(), dest);
        writeMoreHumidity(condition.getHumidity(), dest);
        writeMoreWind(condition.getWind(), dest);
    }
    
    void writeTemperature(Temperature temp, Parcel dest) {
        if (temp != null) {
            dest.writeInt(temp.getCurrent());
            dest.writeInt(temp.getLow());
            dest.writeInt(temp.getHigh());
        } else {
            dest.writeInt(Temperature.UNKNOWN);
            dest.writeInt(Temperature.UNKNOWN);
            dest.writeInt(Temperature.UNKNOWN);
        }
    }
    
    void writeMoreTemperature(Temperature temp, Parcel dest) {
        if (temp != null) {
            dest.writeString(temp.getTemperatureUnit().toString());
        } else {
            dest.writeString(null);
        }
    }
    
    void writeHumidity(Humidity humidity, Parcel dest) {
        if (humidity != null) {
            dest.writeString(humidity.getText());
        } else {
            dest.writeString(null);
        }
    }
    
    void writeMoreHumidity(Humidity humidity, Parcel dest) {
        if (humidity != null) {
            dest.writeInt(humidity.getValue());
        } else {
            dest.writeInt(Humidity.UNKNOWN);
        }
    }
    
    void writeWind(Wind wind, Parcel dest) {
        if (wind != null) {
            dest.writeString(wind.getText());
        } else {
            dest.writeString(null);
        }
    }
    
    void writeMoreWind(Wind wind, Parcel dest) {
        if (wind != null) {
            dest.writeString(wind.getSpeedUnit().toString());
            dest.writeInt(wind.getSpeed());
            dest.writeString(wind.getDirection().toString());
        } else {
            dest.writeString(null);
            dest.writeInt(Wind.UNKNOWN);
            dest.writeString(null);
        }
    }
    
    private ParcelableWeather(Parcel in) {
        //TODO: keep the set and the order of the original values for backward compatibility
        setLocation(new SimpleLocation(in.readString()));
        setTime(new Date(in.readLong()));
        TemperatureUnit tunit = TemperatureUnit.F;
        try {
            tunit = TemperatureUnit.valueOf(in.readString());
        } catch (Exception e) {
            //using the default value
        }
        WindSpeedUnit wsunit = WindSpeedUnit.MPH;
        try {
            wsunit = WindSpeedUnit.valueOf(in.readString());
        } catch (Exception e) {
            //using the default value
        }
        List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
        while (in.dataAvail() > 6) {    //each condition takes 6 positions
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText(in.readString());
            SimpleTemperature temp = new SimpleTemperature(tunit);
            temp.setCurrent(in.readInt(), tunit);
            temp.setLow(in.readInt(), tunit);
            temp.setHigh(in.readInt(), tunit);
            condition.setTemperature(temp);
            
            SimpleHumidity hum = new SimpleHumidity();
            hum.setValue(in.readInt());
            hum.setText(in.readString());
            condition.setHumidity(hum);
            
            SimpleWind wind = new SimpleWind(wsunit);
            wind.setSpeed(in.readInt(), wsunit);
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
