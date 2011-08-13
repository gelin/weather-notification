/**
 * 
 */
package ru.gelin.android.weather;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vladimir
 *
 */
public class SimpleHumidity implements Humidity {

    private static final Pattern PARSE_PATTERN = Pattern.compile("(\\d++)");

    int value = UNKNOWN;
    String text = "";

    /**
     *  Sets the current humidity.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     *  Sets the current humidity.
     */
    public void setText(String text) {
        if (text.length() > 0) {
            this.text = text;
        }
    }

    /**
     *  Sets the current humidity.
     */
    public void setTextParse(String text) {
        if (text.length() > 0) {
            parseText(text);
        }
    }

    //@Override
    public int getValue() {
        if (this.value == UNKNOWN) {
            return 0; 
        }
        return this.value;
    }

    //@Override
    public String getText() {
        return this.text;
    }

    /**
     * Extract from string humidity value
     */
    void parseText(String text) {
        //TODO: add tests
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.value  = Integer.parseInt(matcher.group(1));   //TODO: catch when non-integer
        }
    }

}
