/*
 *  Android Weather Notification.
 *  Copyright (C) 2011  Denis Nelubin aka Gelin
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

import android.test.AndroidTestCase;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateTimeFormatTest  extends AndroidTestCase {
    
    //static final String FORMAT = "{0,choice,15#<15m|30#<30m|60#<1h|120#<2h|180#<3h|240#<4h|300#<5h|360#<6h|720#<12h|1440#<1d|2880#<2d|2880<>2d}";
    static final String FORMAT = "{0,choice,0#'<'15m|15#'<'30m|30#'<'1h|60#'<'2h|120#'<'3h|180#'<'4h|240#'<'5h|300#'<'6h|360#'<'12h|720#'<'1d|1440#'<'2d|2880#'>'2d}";
    
    Date now;
    
    public void setUp() {
        this.now = new Date();
    }
    
    public void test15Min() {
        assertEquals("<15m", format(addMinutes(this.now, -10)));
        assertEquals("<15m", format(addMinutes(this.now, -14)));
        assertEquals("<30m", format(addMinutes(this.now, -15)));
    }
    
    public void test30Min() {
        assertEquals("<30m", format(addMinutes(this.now, -29)));
    }
    
    public void test2Hours() {
        assertEquals("<2h", format(addMinutes(this.now, -2 * 60 + 1)));
    }
    
    public void test3Hours() {
        assertEquals("<3h", format(addMinutes(this.now, -3 * 60 + 1)));
    }
    
    public void test4Hours() {
        assertEquals("<4h", format(addMinutes(this.now, -4 * 60 + 1)));
    }
    
    public void test5Hours() {
        assertEquals("<5h", format(addMinutes(this.now, -5 * 60 + 1)));
    }
    
    public void test6Hours() {
        assertEquals("<6h", format(addMinutes(this.now, -6 * 60 + 1)));
    }
    
    public void test12Hours() {
        assertEquals("<12h", format(addMinutes(this.now, -12 * 60 + 1)));
    }
    
    public void test1Day() {
        assertEquals("<1d", format(addMinutes(this.now, -24 * 60 + 1)));
    }
    
    public void test2Days() {
        assertEquals("<2d", format(addMinutes(this.now, -2 * 24 * 60 + 1)));
    }
    
    public void testMore2Days() {
        assertEquals(">2d", format(addMinutes(this.now, -2 * 24 * 60)));
    }
    
    String format(Date updated) {
        return MessageFormat.format(FORMAT, getMinutes(updated));
    }
    
    long getMinutes(Date updated) {
        return (this.now.getTime() - updated.getTime()) / 60000;
    }
    
    Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

}
