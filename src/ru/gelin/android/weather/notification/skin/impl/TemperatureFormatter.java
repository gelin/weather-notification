package ru.gelin.android.weather.notification.skin.impl;

import ru.gelin.android.weather.Temperature;

public class TemperatureFormatter {
	
    public String format(int temp) {
        if (temp == Temperature.UNKNOWN) {
            return "";
        }
        return signedValue(temp) + "\u00B0";
    }
	
    public String format(int temp, TemperatureUnit unit) {
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
    
    public String format(int tempC, int tempF, TemperatureUnit unit) {
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
    
    /**
     *  Returns the int value with a sign.
     *  This implementation returns only minus sign for negative values.
     */
    protected String signedValue(int value) {
        return String.valueOf(value);
    }

}
