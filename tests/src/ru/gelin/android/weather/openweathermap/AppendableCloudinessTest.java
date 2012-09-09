package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.*;

public class AppendableCloudinessTest extends AndroidTestCase {

    public void testAppend() {
        AppendableCloudiness cloudiness = new AppendableCloudiness(CloudinessUnit.PERCENT);
        assertEquals(Cloudiness.UNKNOWN, cloudiness.getValue());
        SimpleCloudiness append1 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        append1.setValue(0, CloudinessUnit.PERCENT);
        cloudiness.append(append1);
        assertEquals(0, cloudiness.getValue());
        SimpleCloudiness append2 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        append2.setValue(50, CloudinessUnit.PERCENT);
        cloudiness.append(append2);
        assertEquals(25, cloudiness.getValue());
        SimpleCloudiness append3 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        append3.setValue(100, CloudinessUnit.PERCENT);
        cloudiness.append(append3);
        assertEquals(50, cloudiness.getValue());
    }

    public void testAppendConvert() {
        AppendableCloudiness cloudiness = new AppendableCloudiness(CloudinessUnit.PERCENT);
        assertEquals(Cloudiness.UNKNOWN, cloudiness.getValue());
        SimpleCloudiness append1 = new SimpleCloudiness(CloudinessUnit.OKTA);
        append1.setValue(4, CloudinessUnit.OKTA);
        cloudiness.append(append1);
        assertEquals(50, cloudiness.getValue());
    }

    public void testAppendUnknown() {
        AppendableCloudiness cloudiness = new AppendableCloudiness(CloudinessUnit.PERCENT);
        assertEquals(Cloudiness.UNKNOWN, cloudiness.getValue());
        SimpleCloudiness append1 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        cloudiness.append(append1);
        assertEquals(Cloudiness.UNKNOWN, cloudiness.getValue());
        SimpleCloudiness append2 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        append2.setValue(50, CloudinessUnit.PERCENT);
        cloudiness.append(append2);
        assertEquals(50, cloudiness.getValue());
        SimpleCloudiness append3 = new SimpleCloudiness(CloudinessUnit.PERCENT);
        cloudiness.append(append3);
        assertEquals(50, cloudiness.getValue());
    }

}
