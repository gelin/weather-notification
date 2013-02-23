package ru.gelin.android.weather.notification.skin.bigtext;

/**
 *  This formatter displays "+" sign for positive temperature.
 */
public class TemperatureFormat extends ru.gelin.android.weather.notification.skin.impl.TemperatureFormat {

    protected String signedValue(int value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }

}
