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
