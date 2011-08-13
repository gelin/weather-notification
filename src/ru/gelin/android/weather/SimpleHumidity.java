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
    int current = UNKNOWN;
    private static final Pattern PARSE_PATTERN = Pattern.compile("(\\d++)");
    String humtext = "";

    /**
     *  Sets the current humidity.
     */
    public void setValue(int hum) {
        this.current = hum;
    }
    
    /**
     *  Sets the current humidity.
     */
    public void setText(String hum) {
    	if (hum.length() > 0) {
    		this.humtext = hum;
    	}
    }
    
    /**
     *  Sets the current humidity.
     */
    public void setTextParse(String hum) {
    	if (hum.length() > 0) {
    		parseText(hum);
    	}
    }
    
    //@Override
    public int getValue() {
        if (this.current == UNKNOWN) {
            return 0; 
        }
        return this.current;
    }

    //@Override
    public String getText() {
        return this.humtext;
    }
    
    /**
     * Extract from string humidity value
     */
    void parseText(String text) {
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.current  = Integer.parseInt(matcher.group(1));
        }
    }

}
