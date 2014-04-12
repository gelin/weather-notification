package ru.gelin.android.weather.notification.skin.yotaphone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.yotadevices.sdk.notifications.BSNotification;
import com.yotadevices.sdk.notifications.BSNotificationManager;
import ru.gelin.android.weather.Temperature;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.WeatherCondition;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory;
import ru.gelin.android.weather.notification.skin.impl.TemperatureFormat;
import ru.gelin.android.weather.notification.skin.impl.TemperatureType;

import java.util.Calendar;
import java.util.Date;

import static ru.gelin.android.weather.notification.skin.impl.PreferenceKeys.*;
import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;


/**
 *  Invisible activity to start BSNotificationManager.
 *  It binds service internally which is not possible to do from BroadcastReceiver.
 */
public class ShowNotificationActivity extends Activity {

    private static final String SEPARATOR = " ";

    /** Temperature formatter */
    protected TemperatureFormat tempFormat = createTemperatureFormat();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeatherStorage storage = new WeatherStorage(this);
        Weather weather = storage.load();

        ResourceIdFactory ids = ResourceIdFactory.getInstance(this);
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        TemperatureType unit = TemperatureType.valueOf(prefs.getString(
                TEMP_UNIT, TEMP_UNIT_DEFAULT));
        TemperatureUnit mainUnit = unit.getTemperatureUnit();
//        NotificationStyle textStyle = NotificationStyle.valueOf(prefs.getString(
//                NOTIFICATION_TEXT_STYLE, NOTIFICATION_TEXT_STYLE_DEFAULT));

        BSNotification.Builder builder = new BSNotification.Builder();

//        builder.setSmallIcon(getNotificationIconId());

        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
//            notification.tickerText = context.getString(ids.id(STRING, "unknown_weather"));
        } else {
//            notification.tickerText = formatTicker(context, weather, unit);
//            notification.iconLevel = getNotificationIconLevel(weather, mainUnit);
        }

        builder.setWhen(weather.getTime().getTime());
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;

//        notification.contentView = new RemoteViews(context.getPackageName(),
//                getNotificationLayoutId(context, textStyle, unit));
//        RemoteWeatherLayout layout = createRemoteWeatherLayout(
//                context, notification.contentView, unit);
//        layout.bind(weather);
        builder.setContentTitle(formatTitle(this, weather, unit));
        builder.setContentText(formatText(this, ids, weather, mainUnit));

//        notification.contentIntent = getContentIntent(context);
        //notification.contentIntent = getMainActivityPendingIntent(context);

        getBSNotificationManager(this).notify(getNotificationId(), builder.build());

    }

    /**
     *  Returns the notification ID for the skin.
     *  Different skins withing the same application must return different results here.
     */
    protected int getNotificationId() {
        return this.getClass().getName().hashCode();
    }

    protected String formatTitle(Context context, Weather weather, TemperatureType unit) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Temperature tempC = condition.getTemperature(TemperatureUnit.C);
        Temperature tempF = condition.getTemperature(TemperatureUnit.F);
        return context.getString(ids.id(STRING, "notification_ticker"),
                weather.getLocation().getText(),
                tempFormat.format(tempC.getCurrent(), tempF.getCurrent(), unit));
    }

    protected String formatText(Context context, ResourceIdFactory ids, Weather weather, TemperatureUnit unit) {
        StringBuilder forecastsText = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            if (weather.getConditions().size() <= i) {
                break;
            }
            WeatherCondition forecast = weather.getConditions().get(i);
            Temperature temp = forecast.getTemperature(unit);
            Date day = addDays(weather.getTime(), i);
            forecastsText.append(context.getString(ids.id(ResourceIdFactory.STRING, "forecast_text"),
                    day,
                    tempFormat.format(temp.getHigh()),
                    tempFormat.format(temp.getLow())));
            forecastsText.append(SEPARATOR);
        }
        return forecastsText.toString();
    }

    Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     *  Creates the temperature formatter.
     */
    protected TemperatureFormat createTemperatureFormat() {
        return new TemperatureFormat();
    }

    BSNotificationManager getBSNotificationManager(Context context) {
        return new BSNotificationManager(context);
    }

}