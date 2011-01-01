package ru.gelin.android.weather;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleWeatherConditionTest {
    
    SimpleWeatherCondition condition;
    
    @Before
    public void setUp() {
        condition = new SimpleWeatherCondition();
    }
    
    @Test
    public void testTempInUnits() {
        SimpleTemperature temp = new SimpleTemperature(UnitSystem.SI);
        temp.setCurrent(25, UnitSystem.SI);
        condition.setTemperature(temp);
        Temperature temp1 = condition.getTemperature();
        assertEquals(25, temp1.getCurrent());
        Temperature temp2 = condition.getTemperature(UnitSystem.US);
        assertEquals(77, temp2.getCurrent());
    }

}
