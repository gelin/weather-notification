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

package ru.gelin.android.weather;

import android.test.AndroidTestCase;

@SuppressWarnings("deprecation")
public class SimpleTemperatureTest extends AndroidTestCase {
    
    public void testConstructor() {
        SimpleTemperature temp1 = new SimpleTemperature(UnitSystem.SI);
        assertEquals(UnitSystem.SI, temp1.getUnitSystem());
        SimpleTemperature temp2 = new SimpleTemperature(UnitSystem.US);
        assertEquals(UnitSystem.US, temp2.getUnitSystem());
    }
    
    public void testSetLowHigh() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setCurrent(25, UnitSystem.SI);
        assertEquals(25, temp.getLow());
        assertEquals(25, temp.getHigh());
    }
    
    public void testSetLowCurrent() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setHigh(25, UnitSystem.SI);
        assertEquals(25, temp.getLow());
        assertEquals(25, temp.getHigh());
        assertEquals(25, temp.getCurrent());
    }
    
    public void testSetHighCurrent() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setLow(25, UnitSystem.SI);
        assertEquals(25, temp.getLow());
        assertEquals(25, temp.getHigh());
        assertEquals(25, temp.getCurrent());
    }
    
    public void testSetCurrent() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setLow(25, UnitSystem.SI);
        temp.setHigh(35, UnitSystem.SI);
        assertEquals(25, temp.getLow());
        assertEquals(35, temp.getHigh());
        assertEquals(30, temp.getCurrent());
    }
    
    public void testSetCurrent2() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setLow(25, UnitSystem.SI);
        temp.setHigh(28, UnitSystem.SI);
        assertEquals(25, temp.getLow());
        assertEquals(28, temp.getHigh());
        assertEquals(27, temp.getCurrent());
    }
    
    public void testSetAll() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setLow(25, UnitSystem.SI);
        temp.setHigh(35, UnitSystem.SI);
        temp.setCurrent(32, UnitSystem.SI);
        assertEquals(25, temp.getLow());
        assertEquals(35, temp.getHigh());
        assertEquals(32, temp.getCurrent());
    }
    
    public void testConvertSI2US() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.US);
        temp.setLow(25, UnitSystem.SI);
        temp.setHigh(35, UnitSystem.SI);
        temp.setCurrent(32, UnitSystem.SI);
        assertEquals(77, temp.getLow());
        assertEquals(95, temp.getHigh());
        assertEquals(90, temp.getCurrent());
    }
    
    public void testConvertUS2SI() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setLow(77, UnitSystem.US);
        temp.setHigh(95, UnitSystem.US);
        temp.setCurrent(90, UnitSystem.US);
        assertEquals(25, temp.getLow());
        assertEquals(35, temp.getHigh());
        assertEquals(32, temp.getCurrent());
    }
    
    public void testConvert() {
        SimpleTemperature temp1 = new SimpleTemperature(UnitSystem.SI);
        temp1.setLow(25, UnitSystem.SI);
        temp1.setHigh(35, UnitSystem.SI);
        temp1.setCurrent(32, UnitSystem.SI);
        SimpleTemperature temp2 = temp1.convert(UnitSystem.US);
        assertEquals(UnitSystem.US, temp2.getUnitSystem());
        assertEquals(77, temp2.getLow());
        assertEquals(95, temp2.getHigh());
        assertEquals(90, temp2.getCurrent());
    }
    
    public void testConvertUnknownValue() {
        SimpleTemperature temp1 = new SimpleTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temp1.getCurrent());
        assertEquals(Temperature.UNKNOWN, temp1.getHigh());
        assertEquals(Temperature.UNKNOWN, temp1.getLow());
        SimpleTemperature temp2 = temp1.convert(TemperatureUnit.F);
        assertEquals(Temperature.UNKNOWN, temp2.getCurrent());
        assertEquals(Temperature.UNKNOWN, temp2.getHigh());
        assertEquals(Temperature.UNKNOWN, temp2.getLow());
        SimpleTemperature temp3 = temp2.convert(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temp3.getCurrent());
        assertEquals(Temperature.UNKNOWN, temp3.getHigh());
        assertEquals(Temperature.UNKNOWN, temp3.getLow());
    }

    public void testConvertKtoC() {
        SimpleTemperature temp = new SimpleTemperature(TemperatureUnit.K);
        temp.setLow(288, TemperatureUnit.K);
        temp.setHigh(288 + 15, TemperatureUnit.K);
        temp.setCurrent(288 + 5, TemperatureUnit.K);
        SimpleTemperature temp2 = temp.convert(TemperatureUnit.C);
        assertEquals(15, temp2.getLow());
        assertEquals(30, temp2.getHigh());
        assertEquals(20, temp2.getCurrent());
    }

    public void testConvertKtoF() {
        SimpleTemperature temp = new SimpleTemperature(TemperatureUnit.K);
        temp.setLow(273 + 25, TemperatureUnit.K);
        temp.setHigh(273 + 35, TemperatureUnit.K);
        temp.setCurrent(273 + 32, TemperatureUnit.K);
        SimpleTemperature temp2 = temp.convert(TemperatureUnit.F);
        assertEquals(77, temp2.getLow());
        assertEquals(95, temp2.getHigh());
        assertEquals(89, temp2.getCurrent());
    }
    
    // demonstration to avoid multiple convertions
    /*
    public void testMultipleConvert() {
        SimpleTemperature temp1 = new SimpleTemperature(TemperatureUnit.F);
        temp1.setCurrent(-10, TemperatureUnit.F);
        SimpleTemperature temp2 = temp1.convert(TemperatureUnit.C);
        assertEquals(-23, temp2.getCurrent());
        SimpleTemperature temp3 = temp2.convert(TemperatureUnit.F);
        assertEquals(-10, temp3.getCurrent());
    }
    */

}
