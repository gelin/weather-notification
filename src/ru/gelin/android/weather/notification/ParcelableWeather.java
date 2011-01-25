package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.PreferenceKeys.UNIT_SYSTEM_DEFAULT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.gelin.android.weather.Location;
import ru.gelin.android.weather.SimpleLocation;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.SimpleWeather;
import ru.gelin.android.weather.SimpleWeatherCondition;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.UnitSystem;
import ru.gelin.android.weather.WeatherCondition;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableWeather extends SimpleWeather implements Parcelable {

    public ParcelableWeather() {
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
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
        UnitSystem unit = getUnitSystem();
        if (unit == null) {
            dest.writeString(null);
        } else {
            dest.writeString(unit.toString());
        }
        int i = 0;
        if (getConditions() == null) {
            return;
        }
        for (WeatherCondition condition : getConditions()) {
            dest.writeString(condition.getConditionText());
            Temperature temp = condition.getTemperature();
            if (temp == null) {
                continue;
            }
            dest.writeInt(temp.getCurrent());
            dest.writeInt(temp.getLow());
            dest.writeInt(temp.getHigh());
            dest.writeString(condition.getHumidityText());
            dest.writeString(condition.getWindText());
            i++;
        }
    }
    
    private ParcelableWeather(Parcel in) {
        setLocation(new SimpleLocation(in.readString()));
        setTime(new Date(in.readLong()));
        String unit = in.readString();
        try {
            setUnitSystem(UnitSystem.valueOf(unit));
        } catch (Exception e) {
            setUnitSystem(UnitSystem.valueOf(UNIT_SYSTEM_DEFAULT));
        }
        List<WeatherCondition> conditions = new ArrayList<WeatherCondition>();
        while (in.dataAvail() > 6) {    //each condition takes 6 positions
            SimpleWeatherCondition condition = new SimpleWeatherCondition();
            condition.setConditionText(in.readString());
            SimpleTemperature temp = new SimpleTemperature(getUnitSystem());
            temp.setCurrent(in.readInt(), getUnitSystem());
            temp.setLow(in.readInt(), getUnitSystem());
            temp.setHigh(in.readInt(), getUnitSystem());
            condition.setTemperature(temp);
            condition.setHumidityText(in.readString());
            condition.setWindText(in.readString());
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
