package ru.gelin.android.weather;

public interface Humidity {
	/** Unknown temperature value */
    static int UNKNOWN = Integer.MIN_VALUE;
    
    /**
     *  Current humidity.
     */
    int getValue();
    
    /**
     *  Returns humidity as a human readable text.
     *  Can return null. 
     */
    String getText();
}
