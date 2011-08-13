package ru.gelin.android.weather;

public interface Wind {
	/** Unknown temperature value */
    static int UNKNOWN = Integer.MIN_VALUE;
    
    /**
     *  Current localized direction.
     */
    WindDirection getDirection();
    
    /**
     *  Current wind speed in default units.
     */
    int getSpeed();
    
    /**
     *  Units of this weather.
     */
    WindSpeedUnit getSpeedUnit();
    
    /**
     *  Returns wind conditions as a human readable text.
     *  Can return null.
     */
    String getText();
}
