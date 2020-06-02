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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Test
    public void testConvertText() {
        SimpleWind wind = new SimpleWind(WindSpeedUnit.MPH);
        wind.setText("text");
        SimpleWind wind2 = wind.convert(WindSpeedUnit.MPS);
        assertEquals("text", wind2.getText());
    }

}
