package ru.gelin.android.weather.openweathermap;

import ru.gelin.android.weather.*;

/**
 *  Special condition to hold additional data available by OpenWeatherMap.
 */
public class OpenWeatherMapWeatherCondition extends SimpleWeatherCondition {

    SimplePrecipitation precipitation;
    SimpleCloudiness cloudiness;

    public void setPrecipitation(SimplePrecipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Precipitation getPrecipitation() {
        return this.precipitation;
    }

    public void setCloudiness(SimpleCloudiness cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Cloudiness getCloudiness() {
        return this.cloudiness;
    }

    public Cloudiness getCloudiness(CloudinessUnit unit) {
        return this.cloudiness.convert(unit);
    }

    public WeatherConditionType getConditionType() {
        float precipitation = 0f;
        if (getPrecipitation() != null) {
            precipitation = getPrecipitation().getValue(PrecipitationPeriod.PERIOD_1H);
        }
        int cloudiness = 0;
        if (getCloudiness() != null) {
            cloudiness = getCloudiness(CloudinessUnit.OKTA).getValue();
        }
        if (precipitation > 50) {
            return WeatherConditionType.EXTREME_PREC;
        } else if (precipitation > 4) {
            return WeatherConditionType.HEAVY_PREC;
        } else if (precipitation > 1) {
            return WeatherConditionType.MODERATE_PREC;
        } else if (precipitation > 0.1) {
            switch (cloudiness) {
                case 0: return WeatherConditionType.SKC_PREC;
                case 1:case 2: return WeatherConditionType.FEW_PREC;
                case 3:case 4: return WeatherConditionType.SCT_PREC;
                case 5:case 6:case 7: return WeatherConditionType.BKN_PREC;
                case 8: return WeatherConditionType.OVC_PREC;
                default: return WeatherConditionType.SKC_PREC;
            }
        } else {
            switch (cloudiness) {
                case 0: return WeatherConditionType.SKC;
                case 1:case 2: return WeatherConditionType.FEW;
                case 3:case 4: return WeatherConditionType.SCT;
                case 5:case 6:case 7: return WeatherConditionType.BKN;
                case 8: return WeatherConditionType.OVC;
                default: return WeatherConditionType.SKC;
            }
        }
    }



}
