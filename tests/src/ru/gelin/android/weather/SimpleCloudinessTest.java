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
public class SimpleCloudinessTest extends AndroidTestCase {
    
    public void testConvertOkta2Percent() {
        internalTestOkta2Percent(0, 0);
        internalTestOkta2Percent(1, 12);
        internalTestOkta2Percent(2, 25);
        internalTestOkta2Percent(3, 37);
        internalTestOkta2Percent(4, 50);
        internalTestOkta2Percent(5, 62);
        internalTestOkta2Percent(6, 75);
        internalTestOkta2Percent(7, 87);
        internalTestOkta2Percent(8, 100);
    }

    void internalTestOkta2Percent(int okta, int percent) {
        SimpleCloudiness cloud1 = new SimpleCloudiness(CloudinessUnit.OKTA);
        cloud1.setValue(okta, CloudinessUnit.OKTA);
        SimpleCloudiness cloud2 = cloud1.convert(CloudinessUnit.PERCENT);
        assertEquals(CloudinessUnit.PERCENT, cloud2.getCloudinessUnit());
        assertEquals(percent, cloud2.getValue());
    }

    public void testConvertPercent2Okta() {
        internalTestPercent2Okta(0, 0);
        internalTestPercent2Okta(15, 1);
        internalTestPercent2Okta(25, 2);
        internalTestPercent2Okta(40, 3);
        internalTestPercent2Okta(50, 4);
        internalTestPercent2Okta(65, 5);
        internalTestPercent2Okta(75, 6);
        internalTestPercent2Okta(88, 7);
        internalTestPercent2Okta(98, 8);
    }

    void internalTestPercent2Okta(int percent, int okta) {
        SimpleCloudiness cloud1 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        cloud1.setValue(percent, CloudinessUnit.PERCENT);
        SimpleCloudiness cloud2 = cloud1.convert(CloudinessUnit.OKTA);
        assertEquals(CloudinessUnit.OKTA, cloud2.getCloudinessUnit());
        assertEquals(okta, cloud2.getValue());
    }

}
