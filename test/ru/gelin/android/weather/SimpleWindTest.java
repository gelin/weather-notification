/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin
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

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleWindTest {
    
    @Test
    public void testDefaultValues() {
        Wind wind = new SimpleWind(WindSpeedUnit.MPS);
        assertEquals(Wind.UNKNOWN, wind.getSpeed());
        assertEquals(WindSpeedUnit.MPS, wind.getSpeedUnit());
        assertNull(wind.getText());
    }
    
    @Test
    public void testSetSpeedMPS() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPS);
        wind.setSpeed(10, WindSpeedUnit.MPS);
        assertEquals(10, wind.getSpeed());
        wind.setSpeed(10, WindSpeedUnit.KMPH);
        assertEquals(3, wind.getSpeed());
        wind.setSpeed(10, WindSpeedUnit.MPH);
        assertEquals(4, wind.getSpeed());
    }
    
    @Test
    public void testSetSpeedKMPH() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.KMPH);
        wind.setSpeed(10, WindSpeedUnit.MPS);
        assertEquals(36, wind.getSpeed());
        wind.setSpeed(10, WindSpeedUnit.KMPH);
        assertEquals(10, wind.getSpeed());
        wind.setSpeed(10, WindSpeedUnit.MPH);
        assertEquals(16, wind.getSpeed());
    }
    
    @Test
    public void testSetSpeedMPH() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPH);
        wind.setSpeed(10, WindSpeedUnit.MPS);
        assertEquals(22, wind.getSpeed());
        wind.setSpeed(10, WindSpeedUnit.KMPH);
        assertEquals(6, wind.getSpeed());
        wind.setSpeed(10, WindSpeedUnit.MPH);
        assertEquals(10, wind.getSpeed());
    }
    
    @Test
    public void testConvertMPS() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPS);
        wind.setSpeed(10, WindSpeedUnit.MPS);
        Wind kmph = wind.convert(WindSpeedUnit.KMPH);
        assertEquals(WindSpeedUnit.KMPH, kmph.getSpeedUnit());
        assertEquals(36, kmph.getSpeed());
        Wind mph = wind.convert(WindSpeedUnit.MPH);
        assertEquals(WindSpeedUnit.MPH, mph.getSpeedUnit());
        assertEquals(22, mph.getSpeed());
    }
    
    @Test
    public void testConvertKMPH() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.KMPH);
        wind.setSpeed(10, WindSpeedUnit.KMPH);
        Wind mps = wind.convert(WindSpeedUnit.MPS);
        assertEquals(WindSpeedUnit.MPS, mps.getSpeedUnit());
        assertEquals(3, mps.getSpeed());
        Wind mph = wind.convert(WindSpeedUnit.MPH);
        assertEquals(WindSpeedUnit.MPH, mph.getSpeedUnit());
        assertEquals(6, mph.getSpeed());
    }
    
    @Test
    public void testConvertMPH() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPH);
        wind.setSpeed(10, WindSpeedUnit.MPH);
        Wind mps = wind.convert(WindSpeedUnit.MPS);
        assertEquals(WindSpeedUnit.MPS, mps.getSpeedUnit());
        assertEquals(4, mps.getSpeed());
        Wind kmph = wind.convert(WindSpeedUnit.KMPH);
        assertEquals(WindSpeedUnit.KMPH, kmph.getSpeedUnit());
        assertEquals(16, kmph.getSpeed());
    }

}