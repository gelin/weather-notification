package ru.gelin.android.weather.notification;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class WeatherPreference extends Preference {

    public WeatherPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.weather);
    }
    public WeatherPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.weather);
    }
    public WeatherPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.weather);
    }

}
