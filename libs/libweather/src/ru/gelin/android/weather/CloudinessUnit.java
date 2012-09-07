package ru.gelin.android.weather;

/**
 *  Supported cloudiness units.
 */
public enum CloudinessUnit {
    PERCENT("%"), OKTA(" oktas");

    String text = "";

    CloudinessUnit(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}
