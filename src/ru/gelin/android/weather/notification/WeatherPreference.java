package ru.gelin.android.weather.notification;

import ru.gelin.android.weather.Weather;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

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
    
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        WeatherStorage storage = new WeatherStorage(getContext());
        WeatherLayout layout = new WeatherLayout(getContext());
        Weather weather = storage.load();
        layout.bind(weather, view);
    }

}
