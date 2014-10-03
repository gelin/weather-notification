package ru.gelin.android.weather.notification.skin.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.TemperatureUnit;

import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;

/**
 *  A class which holds together: Context, Weather, NotificationStyler -
 *  and provides formatting methods for many aspects of the weather representation.
 */
public class WeatherFormatter {

    /** Large icon size */
    static final int LARGE_ICON = 48;

    private final Context context;
    private final Weather weather;
    private final NotificationStyler styler;
    private final ResourceIdFactory ids;

    public WeatherFormatter(Context context, Weather weather) {
        this.context = context;
        this.weather = weather;
        this.styler = new NotificationStyler(context);
        this.ids = ResourceIdFactory.getInstance(context);
    }

    protected Context getContext() {
        return this.context;
    }

    protected Weather getWeather() {
        return this.weather;
    }

    protected NotificationStyler getStyler() {
        return this.styler;
    }

    protected ResourceIdFactory getIds() {
        return this.ids;
    }

    protected String formatTicker() {
        WeatherCondition condition = getWeather().getConditions().get(0);
        Temperature tempC = condition.getTemperature(TemperatureUnit.C);
        Temperature tempF = condition.getTemperature(TemperatureUnit.F);
        return getContext().getString(
                getIds().id(STRING, "notification_ticker"),
                getWeather().getLocation().getText(),
                getTemperatureFormat().format(
                        tempC.getCurrent(),
                        tempF.getCurrent(),
                        getStyler().getTempType()));
    }

    protected String formatContentTitle() {
        WeatherCondition condition = getWeather().getConditions().get(0);
        Temperature tempC = condition.getTemperature(TemperatureUnit.C);
        Temperature tempF = condition.getTemperature(TemperatureUnit.F);
        return getContext().getString(
                getIds().id(STRING, "notification_content_title"),
                getTemperatureFormat().format(
                        tempC.getCurrent(),
                        tempF.getCurrent(),
                        getStyler().getTempType()),
                getWeatherConditionFormat().getText(condition));
    }

    protected String formatContentText() {
        WeatherCondition condition = getWeather().getConditions().get(0);

        TemperatureFormat tempFormat = getTemperatureFormat();
        TemperatureUnit tempUnit = getStyler().getTempType().getTemperatureUnit();
        Temperature temp = condition.getTemperature(tempUnit);

        Wind wind = condition.getWind(getStyler().getWindUnit().getWindSpeedUnit());
        Humidity humidity = condition.getHumidity();

        return getContext().getString(getIds().id(STRING, "notification_content_text"),
                tempFormat.format(temp.getHigh()),
                tempFormat.format(temp.getLow()),
                getWindFormat().format(wind),
                getHumidityFormat().format(humidity));
    }

    protected Bitmap formatLargeIcon() {
        WeatherCondition condition = getWeather().getConditions().get(0);

        WeatherConditionFormat format = getWeatherConditionFormat();
        Drawable drawable = format.getDrawable(condition);
        drawable.setLevel(LARGE_ICON);
        return Drawable2Bitmap.convert(drawable);
    }

    /**
     *  Creates the temperature formatter.
     */
    protected TemperatureFormat getTemperatureFormat() {
        return new TemperatureFormat();
    }

    /**
     *  Creates the weather condition format.
     */
    protected WeatherConditionFormat getWeatherConditionFormat() {
        return new WeatherConditionFormat(getContext());
    }

    /**
     *  Creates the wind format.
     */
    protected WindFormat getWindFormat() {
        return new WindFormat(getContext());
    }

    /**
     *  Creates the humidity format.
     */
    protected HumidityFormat getHumidityFormat() {
        return new HumidityFormat(getContext());
    }

}
