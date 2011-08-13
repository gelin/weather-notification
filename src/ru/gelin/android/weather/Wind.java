package ru.gelin.android.weather;

public interface Wind {
	/** Unknown temperature value */
    static int UNKNOWN = Integer.MIN_VALUE;
    
    /**
     *  Returns wind direction.
     */
    WindDirection getDirection();
    
    /**
     *  Returns wind speed.
     */
    int getSpeed();
    
    /**
     *  Wind speed units for this instance.
     */
    WindSpeedUnit getSpeedUnit();
    
    /**
     *  Returns wind conditions as a human readable text.
     *  Can return null.
     */
    String getText();
    
}
