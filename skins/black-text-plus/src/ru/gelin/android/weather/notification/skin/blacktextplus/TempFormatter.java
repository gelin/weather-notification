package ru.gelin.android.weather.notification.skin.blacktextplus;

import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.notification.skin.impl.TemperatureUnit;

public class TempFormatter {
	
    public static String formatTemp(int temp) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        return signedValue(temp) + "\u00B0";
    }
	
    public static String formatTemp(int temp, TemperatureUnit unit) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        switch (unit) {
        case C:
            return signedValue(temp) + "\u00B0C";
        case F:
            return signedValue(temp) + "\u00B0F";
        case CF:
            return signedValue(temp) + "\u00B0C";
        case FC:
            return signedValue(temp) + "\u00B0F";
        }
        return "";
    }
    
    public static String formatTemp(int tempC, int tempF, TemperatureUnit unit) {
        if (tempC == Temperature.UNKNOWN || tempF == Temperature.UNKNOWN) {
            return "";
        }
        switch (unit) {
        case C:
            return signedValue(tempC) + "\u00B0C";
        case F:
            return signedValue(tempF) + "\u00B0F";
        case CF:
            return signedValue(tempC) + "\u00B0C(" + signedValue(tempF) + "\u00B0F)";
        case FC:
            return signedValue(tempF) + "\u00B0F(" + signedValue(tempC) + "\u00B0C)";
        }
        return "";
    }
    
    static String signedValue(int value) {
        if (value > 0) {
            return "+" + value;
        } else {
            return String.valueOf(value);
        }
    }

}
