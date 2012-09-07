package ru.gelin.android.weather.openweathermap;

import android.test.AndroidTestCase;
import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;

public class AppendableTemperatureTest extends AndroidTestCase {

    public void testAppendLowTemperature() {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temperature.getLow());
        SimpleTemperature append1 = new SimpleTemperature(TemperatureUnit.C);
        append1.setLow(-5, TemperatureUnit.C);
        temperature.append(append1);
        assertEquals(-5, temperature.getLow());
        SimpleTemperature append2 = new SimpleTemperature(TemperatureUnit.C);
        append2.setLow(-10, TemperatureUnit.C);
        temperature.append(append2);
        assertEquals(-10, temperature.getLow());
        SimpleTemperature append3 = new SimpleTemperature(TemperatureUnit.C);
        append3.setLow(-3, TemperatureUnit.C);
        temperature.append(append3);
        assertEquals(-10, temperature.getLow());
    }

    public void testAppendHighTemperature() {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temperature.getHigh());
        SimpleTemperature append1 = new SimpleTemperature(TemperatureUnit.C);
        append1.setHigh(5, TemperatureUnit.C);
        temperature.append(append1);
        assertEquals(5, temperature.getHigh());
        SimpleTemperature append2 = new SimpleTemperature(TemperatureUnit.C);
        append2.setHigh(10, TemperatureUnit.C);
        temperature.append(append2);
        assertEquals(10, temperature.getHigh());
        SimpleTemperature append3 = new SimpleTemperature(TemperatureUnit.C);
        append3.setHigh(3, TemperatureUnit.C);
        temperature.append(append3);
        assertEquals(10, temperature.getHigh());
    }

    public void testAppendTemperatureConvert() {
        AppendableTemperature temperature = new AppendableTemperature(TemperatureUnit.C);
        assertEquals(Temperature.UNKNOWN, temperature.getLow());
        SimpleTemperature append1 = new SimpleTemperature(TemperatureUnit.K);
        append1.setLow(273, TemperatureUnit.K);
        temperature.append(append1);
        assertEquals(0, temperature.getLow());
        assertEquals(0, temperature.getHigh());
    }

}
