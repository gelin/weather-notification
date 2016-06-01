/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather;

/**
 *  Holds wind speed and direction.
 *  Allows to set the wind speed in different units.
 */
public class SimpleWind implements Wind {

    protected WindSpeedUnit wsunit;
    protected int speed = UNKNOWN;
    protected WindDirection direction = WindDirection.N;
    protected String text = null;

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
        result.setText(this.text);
        result.setSpeed(this.speed, this.wsunit);
        result.setDirection(this.direction);
        return result;
    }

    /**
     *  Sets the text representation of the wind speed and direction.
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     *  Sets the wind direction.
     */
    public void setDirection(WindDirection direction) {
        this.direction = direction;
    }

    /**
     *  Sets the wind speed in specified units.
     */
    public void setSpeed(int speed, WindSpeedUnit unit) {
        if (this.wsunit.equals(unit)) {
            this.speed = speed;
        } else {
            this.speed = convert(speed, unit);
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

    int convert(int speed, WindSpeedUnit unit) {
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
