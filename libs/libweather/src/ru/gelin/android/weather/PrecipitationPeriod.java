package ru.gelin.android.weather;

/**
 *  Supported precipitation period values.
 */
public enum PrecipitationPeriod {
    PERIOD_1H(1), PERIOD_3H(3);

    /** Period length in hours */
    int hours;

    PrecipitationPeriod(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return this.hours;
    }

    public static PrecipitationPeriod valueOf(int hours) {
        switch (hours) {
            case 1: return PERIOD_1H;
            case 3: return PERIOD_3H;
            default: throw new IllegalArgumentException(hours + " is not valid value for PrecipitationPeriod");
        }
    }

}
