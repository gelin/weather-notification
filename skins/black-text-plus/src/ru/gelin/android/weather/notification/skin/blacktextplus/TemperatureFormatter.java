package ru.gelin.android.weather.notification.skin.blacktextplus;

/**
 *  This formatter displays "+" sign for positive temperature.
 */
public class TemperatureFormatter extends ru.gelin.android.weather.notification.skin.impl.TemperatureFormatter {

    protected String signedValue(int value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }

}
