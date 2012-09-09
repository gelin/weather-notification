package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.*;

public class AppendablePrecipitationTest extends AndroidTestCase {

    public void testAppend() {
        AppendablePrecipitation precipitation = new AppendablePrecipitation(PrecipitationUnit.MM);
        assertEquals(Precipitation.UNKNOWN, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
        SimplePrecipitation append1 = new SimplePrecipitation(PrecipitationUnit.MM);
        append1.setValue(0, PrecipitationPeriod.PERIOD_1H);
        precipitation.append(append1);
        assertEquals(0f, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
        SimplePrecipitation append2 = new SimplePrecipitation(PrecipitationUnit.MM);
        append2.setValue(3, PrecipitationPeriod.PERIOD_3H);
        precipitation.append(append2);
        assertEquals(3f/4f, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
    }

    public void testAppendUnknown() {
        AppendablePrecipitation precipitation = new AppendablePrecipitation(PrecipitationUnit.MM);
        assertEquals(Precipitation.UNKNOWN, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
        SimplePrecipitation append1 = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.append(append1);
        assertEquals(Precipitation.UNKNOWN, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
        SimplePrecipitation append2 = new SimplePrecipitation(PrecipitationUnit.MM);
        append2.setValue(3, PrecipitationPeriod.PERIOD_3H);
        precipitation.append(append2);
        assertEquals(1f, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
        SimplePrecipitation append3 = new SimplePrecipitation(PrecipitationUnit.MM);
        precipitation.append(append3);
        assertEquals(1f, precipitation.getValue(PrecipitationPeriod.PERIOD_1H));
    }

}
