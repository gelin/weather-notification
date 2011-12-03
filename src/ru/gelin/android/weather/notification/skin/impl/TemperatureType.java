/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
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
