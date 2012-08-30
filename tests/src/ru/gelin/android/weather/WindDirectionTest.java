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

import android.test.AndroidTestCase;

public class WindDirectionTest extends AndroidTestCase {

    public void testConvertWindDirection() {
        assertEquals(WindDirection.NW, WindDirection.valueOf(-45 - 360));
        assertEquals(WindDirection.NW, WindDirection.valueOf(-45));
        assertEquals(WindDirection.N, WindDirection.valueOf(0));
        assertEquals(WindDirection.NNE, WindDirection.valueOf(22));
        assertEquals(WindDirection.NE, WindDirection.valueOf(45));
        assertEquals(WindDirection.E, WindDirection.valueOf(90));
        assertEquals(WindDirection.SE, WindDirection.valueOf(135));
        assertEquals(WindDirection.S, WindDirection.valueOf(180));
        assertEquals(WindDirection.SW, WindDirection.valueOf(225));
        assertEquals(WindDirection.W, WindDirection.valueOf(270));
        assertEquals(WindDirection.NW, WindDirection.valueOf(315));
        assertEquals(WindDirection.N, WindDirection.valueOf(360));
        assertEquals(WindDirection.NE, WindDirection.valueOf(405));
    }

}