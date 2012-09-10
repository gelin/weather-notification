package ru.gelin.android.weather.openweathermap;

import android.content.Context;
import ru.gelin.android.weather.notification.R;

/**
 *  Formats weather condition text.
 */
public class WeatherConditionFormat {

    Context context;

    public WeatherConditionFormat(Context context) {
        this.context = context;
    }

    public String getText(OpenWeatherMapWeatherCondition condition) {
        return context.getString(getStringId(condition));
    }

    int getStringId(OpenWeatherMapWeatherCondition condition) {
        switch (condition.getConditionType()) {
            case SKC: return R.string.condition_skc;
            case FEW: return R.string.condition_few;
            case SCT: return R.string.condition_sct;
            case BKN: return R.string.condition_bkn;
            case OVC: return R.string.condition_ovc;
            case SKC_PREC: return R.string.condition_skc_prec;
            case FEW_PREC: return R.string.condition_few_prec;
            case SCT_PREC: return R.string.condition_sct_prec;
            case BKN_PREC: return R.string.condition_bkn_prec;
            case OVC_PREC: return R.string.condition_ovc_prec;
            case MODERATE_PREC: return R.string.condition_moderate_prec;
            case HEAVY_PREC: return R.string.condition_heavy_prec;
            case EXTREME_PREC: return R.string.condition_extreme_prec;
            default: return R.string.condition_skc;
        }
    }

}
