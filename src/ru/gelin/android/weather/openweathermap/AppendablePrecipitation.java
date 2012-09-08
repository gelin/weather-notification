package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.PrecipitationUnit;
import ru.gelin.android.weather.SimplePrecipitation;

/**
 *  The precipitation value which can be appended by another value.
 */
public class AppendablePrecipitation extends SimplePrecipitation {

    public AppendablePrecipitation(PrecipitationUnit unit) {
        super(unit);
    }

    public void append(SimplePrecipitation precipitation) {
        if (getValue() == UNKNOWN) {
            setValue(precipitation.getValue(), precipitation.getHours());
            return;
        }
        int hours = getHours() + precipitation.getHours();
        float value = getValue() + precipitation.getValue();
        setValue(value, hours);
    }

}
