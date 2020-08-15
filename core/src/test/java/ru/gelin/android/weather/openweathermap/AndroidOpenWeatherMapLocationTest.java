package ru.gelin.android.weather.openweathermap;

import android.location.Location;
import android.os.Build;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class AndroidOpenWeatherMapLocationTest {

    @Test
    public void testLocationInArgentinaLocale() {
        Location androidLocation = new Location("TEST");
        androidLocation.setLatitude(-1.23);
        androidLocation.setLongitude(-4.56);

        Locale.setDefault(new Locale("es", "AR"));
        AndroidOpenWeatherMapLocation location = new AndroidOpenWeatherMapLocation(androidLocation);

        assertEquals("lat=-1.23&lon=-4.56", location.getQuery());
        assertEquals("-1.23,-4.56", location.getText());
    }

}
