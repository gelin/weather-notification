package ru.gelin.android.weather;

/**
 *  Simple location which query and text are equal and are set in constructor.
 */
public class SimpleLocation implements Location {

    String text;
    
    /**
     *  Create the location.
     *  @param locationText query and the text value.
     */
    public SimpleLocation(String text) {
        this.text = text;
    }
    
    @Override
    public String getQuery() {
        return this.text;
    }

    @Override
    public String getText() {
        return this.text;
    }

}
