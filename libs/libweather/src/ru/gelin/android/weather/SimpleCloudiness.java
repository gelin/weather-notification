/*
 *  Weather API.
 *  Copyright (C) 2010  Denis Nelubin, Vladimir Kubyshev
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather;

/**
 *  Holds cloudiness value.
 *  Allows to set the value in different units.
 */
public class SimpleCloudiness implements Cloudiness {

    CloudinessUnit unit;

    int cloudiness = UNKNOWN;

    public SimpleCloudiness(CloudinessUnit unit) {
        this.unit = unit;
    }

    /**
     *  Sets the cloudiness value in specified unit system.
     */
    public void setValue(int cloudiness, CloudinessUnit unit) {
        if (cloudiness == UNKNOWN) {
            this.cloudiness = UNKNOWN;
            return;
        }
        this.cloudiness = convertValue(cloudiness, unit);
    }

    public int getValue() {
        return this.cloudiness;
    }

    public CloudinessUnit getCloudinessUnit() {
        return this.unit;
    }

    @Override
    public String getText() {
        StringBuilder result = new StringBuilder();
        result.append("Cloudiness: ");
        if (getValue() == UNKNOWN) {
            result.append("unknown");
            return result.toString();
        }
        result.append(getValue());
        result.append(getCloudinessUnit().getText());
        return result.toString();
    }

    public SimpleCloudiness convert(CloudinessUnit unit) {
        SimpleCloudiness result = new SimpleCloudiness(unit);
        result.setValue(this.getValue(), this.getCloudinessUnit());
        return result;
    }

    /**
     *  Converts the value from provided unit system into this unit system.
     */
    protected int convertValue(int value, CloudinessUnit unit) {
        if (this.unit.equals(unit)) {
            return value;
        }
        if (CloudinessUnit.PERCENT.equals(unit) && CloudinessUnit.OKTA.equals(this.unit)) {
            return (int)Math.round(8.0 / 100 * value);
        } else if (CloudinessUnit.OKTA.equals(unit) && CloudinessUnit.PERCENT.equals(this.unit)) {
            return (int)(100 / 8.0 * value);
        }
        return value;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + 
            " unit: " + this.getCloudinessUnit() +
            " value: " + this.getValue();
    }
    
}
