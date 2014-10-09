package ru.gelin.android.weather.notification.skin.biggertext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.skin.impl.Drawable2Bitmap;

public class WeatherFormatter extends ru.gelin.android.weather.notification.skin.impl.WeatherFormatter {

    public WeatherFormatter(Context context, Weather weather) {
        super(context, weather);
    }

    @Override
    protected Bitmap formatLargeIcon() {
        WeatherCondition condition = getWeather().getConditions().get(0);

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.temp_icon_light);
        drawable.setLevel(condition.getTemperature(getStyler().getTempType().getTemperatureUnit()).getCurrent() +
                SkinWeatherNotificationReceiver.ICON_LEVEL_SHIFT);
        return Drawable2Bitmap.convert(drawable);
    }
    
}
