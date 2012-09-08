package ru.gelin.android.weather;

/**
 *  Simple implementation to hold precipitation value.
 */
public class SimplePrecipitation implements Precipitation {

    /** Unit for precipitations */
    PrecipitationUnit unit = PrecipitationUnit.MM;
    /** Hours of precipitations */
    int hours;
    /** Value of precipitations */
    float value;

    public SimplePrecipitation(PrecipitationUnit unit) {
        this.unit = unit;
    }

    public void setValue(float value, PrecipitationPeriod period) {
        this.value = value;
        this.hours = period.hours;
    }

    @Override
    public float getValue(PrecipitationPeriod period) {
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
