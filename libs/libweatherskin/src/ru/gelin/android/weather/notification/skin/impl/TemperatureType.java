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

package ru.gelin.android.weather.notification.skin.impl;

import ru.gelin.android.weather.TemperatureUnit;

/**
 *  Maps user's preferences into actual temperature unit.
 */
public enum TemperatureType {

    C(TemperatureUnit.C),
    F(TemperatureUnit.F),
    CF(TemperatureUnit.C),
    FC(TemperatureUnit.F);
    
    TemperatureUnit unit;
    
    TemperatureType(TemperatureUnit unit) {
        this.unit = unit;
    }
    
    public TemperatureUnit getTemperatureUnit() {
        return this.unit;
    }
    
    /**
     *  Converts from deprecated class.
     */
    @SuppressWarnings("deprecation")
    public static TemperatureType valueOf(ru.gelin.android.weather.notification.skin.impl.TemperatureUnit unit) {
        switch (unit) {
        case C:
            return  TemperatureType.C;
        case CF:
            return TemperatureType.CF;
        case F:
            return TemperatureType.F;
        case FC:
            return TemperatureType.FC;
        }
        return TemperatureType.C;
    }

}
