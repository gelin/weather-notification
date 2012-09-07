package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.SimpleTemperature;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;

/**
 *  The temperature, which values can be appended by some another temperature, parsed from the forecasts, for example.
 *  This temperature is updated after the append operation.
 *  The Low temperature is updated by the lowest of the current and appending Low temperatures.
 *  The High temperature is updated by the highest of the current and appending High temperatures.
 */
public class AppendableTemperature extends SimpleTemperature {

    public AppendableTemperature(TemperatureUnit unit) {
        super(unit);
    }

    public void append(Temperature temperature) {
        if (getLow() == Temperature.UNKNOWN) {
            setLow(temperature.getLow(), temperature.getTemperatureUnit());
        } else {
            setLow(Math.min(
                    getLow(), convertValue(temperature.getLow(), temperature.getTemperatureUnit())),
                    getTemperatureUnit());
        }
        if (getHigh() == Temperature.UNKNOWN) {
            setHigh(temperature.getHigh(), temperature.getTemperatureUnit());
        } else {
            setHigh(Math.max(
                    getHigh(), convertValue(temperature.getHigh(), temperature.getTemperatureUnit())),
                    getTemperatureUnit());
        }
    }

}
