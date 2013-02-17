package ru.gelin.android.weather;

/**
 *  Simple implementation to hold precipitation value.
 */
public class SimplePrecipitation implements Precipitation {

    /** Unit for precipitations */
    PrecipitationUnit unit = PrecipitationUnit.MM;
    /** Hours of precipitations */
    int hours = 0;
    /** Value of precipitations */
    float value = UNKNOWN;

    public SimplePrecipitation(PrecipitationUnit unit) {
        this.unit = unit;
    }

    public void setValue(float value, PrecipitationPeriod period) {
        this.value = value;
        this.hours = period.hours;
    }

    public void setValue(float value, int hours) {
        this.value = value;
        this.hours = hours;
    }

    public float getValue() {
        return this.value;
    }

    public int getHours() {
        return this.hours;
    }

    @Override
    public float getValue(PrecipitationPeriod period) {
        if (this.value == UNKNOWN) {
            return UNKNOWN;
        }
        return this.value / this.hours * period.getHours();
    }

    @Override
    public PrecipitationUnit getPrecipitationUnit() {
        return this.unit;
    }

    @Override
    public String getText() {
        return "Precipitation: " + getValue(PrecipitationPeriod.PERIOD_1H) + " mm/h";
    }
}
