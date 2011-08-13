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
public class SimpleWind implements Wind {

    private static final Pattern PARSE_PATTERN = Pattern.compile(".*[^:]:\\s(.*[^\\s])\\sat\\s+(.*[^\\s])\\s.*");

    WindSpeedUnit wsunit;
    int speed = UNKNOWN;
    WindDirection direction = WindDirection.N;
    String text = "";

    /**
     *  Constructs the wind.
     *  The stored values will be returned in the specified unit system.
     */
    public SimpleWind(WindSpeedUnit wsunit) {
        this.wsunit = wsunit;
    }

    /**
     *  Creates new wind in another unit system.
     */
    public SimpleWind convert(WindSpeedUnit unit) {
        SimpleWind result = new SimpleWind(unit);
        result.setSpeed(this.speed, this.wsunit);
        result.setDirection(this.direction);
        return result;
    }

    /**
     *  Sets the current .
     */
    public void setText(String text) {
        if (text.length() > 0) {
            this.text = text;
        }
    }

    /**
     *  Sets the current .
     */
    public void setTextParse(String text) {
        if (text.length() > 0) {
            parseText(text);
        }
    }

    public void setDirection(WindDirection direction) {
        this.direction = direction;
    }

    public void setSpeed(int speed, WindSpeedUnit unit) {
        if (this.wsunit.equals(unit)) {
            this.speed = speed;
        } else {
            this.speed = recalc(speed, unit);
        }
    }


    //@Override
    public WindDirection getDirection() {
        return this.direction;
    }

    //@Override
    public int getSpeed() {
        return this.speed;
    }

    //@Override
    public WindSpeedUnit getSpeedUnit() {
        return this.wsunit;
    }

    //@Override
    public String getText() {
        return this.text;
    }

    /**
     * Extract from string wind speed and direction value
     */
    void parseText(String text) {
        //TODO: add tests
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.speed  = Integer.parseInt(matcher.group(2));   //TODO: catch when non-integer
            this.direction  = WindDirection.valueOf(matcher.group(1));  //TODO: catch when invalid value
        }
    }

    int recalc(int speed, WindSpeedUnit unit) {
        switch (wsunit) {
        case MPH:
            switch (unit) {
            case KMPH: 
                return (int)Math.round(speed * 0.6214);
            case MPH: 
                return this.speed;
            case MPS: 
                return (int)Math.round(speed * 2.2370);
            }
        case MPS:
            switch (unit) {
            case KMPH: 
                return (int)Math.round(speed * 0.2778);
            case MPH: 
                return (int)Math.round(speed * 0.4470);
            case MPS: 
                return this.speed;
            }
        case KMPH:
            switch (unit) {
            case KMPH: 
                return this.speed;
            case MPH: 
                return (int)Math.round(speed * 1.6093);
            case MPS: 
                return (int)Math.round(speed * 3.6000);
            }
        }
        return 0;
    }

}
