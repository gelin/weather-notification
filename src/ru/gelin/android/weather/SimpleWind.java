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

	WindSpeedUnit wsunit;
	private static final Pattern PARSE_PATTERN = Pattern.compile(".*[^:]:\\s(.*[^\\s])\\sat\\s+(.*[^\\s])\\s.*");
	int currentspeed = UNKNOWN;
	WindDirection currentdir = WindDirection.N;
    String windtext = "";
    
    /**
     *  Constructs the wind.
     *  The stored values will be returned in the specified unit system.
     */
    public SimpleWind(WindSpeedUnit wsunit) {
        this.wsunit = wsunit;
    }

    int Recalc(int speed, WindSpeedUnit unit)
    {
    	switch (wsunit){
    	case MPH:{
    		switch (unit){
       		case KMPH: return (int)Math.round(speed * 0.6214);
    		case MPH: return this.currentspeed;
    		case MPS: return (int)Math.round(speed * 2.2370);
    		}
    	}
    	case MPS:{
    		switch (unit){
       		case KMPH: return (int)Math.round(speed * 0.2778);
    		case MPH: return (int)Math.round(speed * 0.4470);
    		case MPS: return this.currentspeed;
    		}
    	}
    	case KMPH:{
    		switch (unit){
       		case KMPH: return this.currentspeed;
    		case MPH: return (int)Math.round(speed * 1.6093);
    		case MPS: return (int)Math.round(speed * 3.6000);
    		}
    	}
    	}
    	return 0;
    }

    /*
     * Extract from string wind speed and direction value
     */
    public void ParseText(String text) {
        Matcher matcher = PARSE_PATTERN.matcher(text);
        if (matcher.find()) {
            this.currentspeed  = Integer.parseInt(matcher.group(2));
            this.currentdir  = WindDirection.valueOf(matcher.group(1));
        }
    }
    
    /**
     *  Creates new wind in another unit system.
     */
    public SimpleWind convert(WindSpeedUnit unit) {
        SimpleWind result = new SimpleWind(unit);
        result.setSpeed(this.currentspeed, this.wsunit);
        result.setDirection(this.currentdir);
        return result;
    }
    
	 /**
     *  Sets the current .
     */
    public void setText(String wind) {
    	if (wind.length() > 0) {
    		this.windtext = wind;
    	}
    }
    
    /**
     *  Sets the current .
     */
    public void setTextParse(String wind) {
    	if (wind.length() > 0) {
           	ParseText(wind);
    	}
    }
    
    public void setDirection(WindDirection wind) {
        this.currentdir = wind;
    }
    
    public void setSpeed(int speed, WindSpeedUnit unit) {
    	if (this.wsunit.equals(unit)) {
            this.currentspeed = speed;
        } else {
            this.currentspeed = Recalc(speed, unit);
        }
    }
    
    
	@Override
	public WindDirection getDirection() {
		return this.currentdir;
	}

	@Override
	public int getSpeed() {
		return this.currentspeed;
	}

	@Override
	public WindSpeedUnit getSpeedUnit() {
		return this.wsunit;
	}

	@Override
	public String getText() {
		return this.windtext;
	}

}
