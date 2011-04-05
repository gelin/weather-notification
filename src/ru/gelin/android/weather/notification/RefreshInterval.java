package ru.gelin.android.weather.notification;

/**
 *  Represents the refresh interval.
 *  Handles interval value (in milliseconds).
 */
public enum RefreshInterval {
    
    REFRESH_15M(900 * 1000),
    REFRESH_30M(1800 * 1000),
    REFRESH_1H(3600 * 1000),
    REFRESH_2H(2 * 3600 * 1000),
    REFRESH_3H(3 * 3600 * 1000),
    REFRESH_4H(4 * 3600 * 1000),
    REFRESH_6H(6 * 3600 * 1000),
    REFRESH_12H(12 * 3600 * 1000),
    REFRESH_1D(24 * 3600 * 1000);
    
    long interval;
    
    RefreshInterval(long interval) {
        this.interval = interval;
    }
    
    /**
     *  Returns refresh interval value (in milliseconds).
     */
    public long getInterval() {
        return this.interval;
    }

}
