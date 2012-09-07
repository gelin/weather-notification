package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.Cloudiness;
import ru.gelin.android.weather.CloudinessUnit;
import ru.gelin.android.weather.SimpleCloudiness;

/**
 *  The cloudiness, which values can be appended by some another cloudiness, parsed from the forecasts, for example.
 *  This cloudiness is updated after the append operation.
 *  The updated cloudiness is the average result of all appends.
 */
public class AppendableCloudiness extends SimpleCloudiness {

    int sum = 0;
    int count = 0;

    public AppendableCloudiness(CloudinessUnit unit) {
        super(unit);
    }

    public void append(Cloudiness cloudiness) {
        this.sum += convertValue(cloudiness.getValue(), cloudiness.getCloudinessUnit());
        this.count++;
        setValue(this.sum / this.count, getCloudinessUnit());
    }

}
