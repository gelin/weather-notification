/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin.impl;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import ru.gelin.android.weather.*;
import ru.gelin.android.weather.TemperatureUnit;
import ru.gelin.android.weather.notification.ParcelableWeather2;
import ru.gelin.android.weather.notification.WeatherStorage;
import ru.gelin.android.weather.notification.skin.Tag;

import java.util.List;

import static ru.gelin.android.weather.notification.skin.impl.ResourceIdFactory.STRING;

/**
 *  Weather notification receiver built into basic application.
 */
abstract public class BaseWeatherNotificationReceiver extends
        WeatherNotificationReceiver {

    /** Key to store the weather in the bundle */
    static final String WEATHER_KEY = "weather";

    /** Large icon size */
    static final int LARGE_ICON = 48;

    /** Handler to receive the weather */
    static Handler handler;
    
    /**
     *  Registers the handler to receive the new weather.
     *  The handler is owned by activity which have initiated the update.
     *  The handler is used to update the weather displayed by the activity. 
     */
    static synchronized void registerWeatherHandler(Handler handler) {
        BaseWeatherNotificationReceiver.handler = handler;
    }
    
    /**
     *  Unregisters the weather update handler.
     */
    static synchronized void unregisterWeatherHandler() {
        BaseWeatherNotificationReceiver.handler = null;
    }
    
    @Override
    protected void cancel(Context context) {
        Log.d(Tag.TAG, "cancelling weather");
        getNotificationManager(context).cancel(getNotificationId());
    }

    @Override
    protected void notify(Context context, Weather weather) {
        Log.d(Tag.TAG, "displaying weather: " + weather);
        
        WeatherStorage storage = new WeatherStorage(context);
        storage.save(weather);
        
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        NotificationStyler styler = createStyler(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(getNotificationIconId(weather));

        if (weather.isEmpty() || weather.getConditions().size() <= 0) {
            builder.setTicker(context.getString(ids.id(STRING, "unknown_weather")));
        } else {
            builder.setTicker(formatTicker(context, weather, styler.getTempType()));
            builder.setSmallIcon(getNotificationIconId(weather),
                    getNotificationIconLevel(weather, styler.getTempType().getTemperatureUnit()));
        }

        builder.setWhen(weather.getQueryTime().getTime());
        builder.setOngoing(true);
        builder.setAutoCancel(false);

        builder.setContentIntent(getContentIntent(context));

        Notification notification = builder.build();

        switch (styler.getNotifyStyle()) {
            case CUSTOM_STYLE:
                RemoteViews views = new RemoteViews(context.getPackageName(), styler.getLayoutId());
                RemoteWeatherLayout layout = createRemoteWeatherLayout(context, views, styler);
                layout.bind(weather);
                notification.contentView = views;
                break;
            case STANDARD_STYLE:
                builder.setContentTitle(formatContentTitle(context, weather, styler));
                builder.setContentText(formatContentText(context, weather, styler));
                Bitmap largeIcon = formatLargeIcon(context, weather);
                if (largeIcon != null) {
                    builder.setLargeIcon(largeIcon);
                }
                notification = builder.build();
                break;
        }

        getNotificationManager(context).notify(getNotificationId(), notification);
        
        notifyHandler(weather);
    }

    /**
     *  Returns the notification ID for the skin.
     *  Different skins withing the same application must return different results here.
     */
    protected int getNotificationId() {
        return this.getClass().getName().hashCode();
    }

    /**
     *  Returns the pending intent called on click on notification.
     *  This intent starts the weather info activity.
     */
    protected PendingIntent getContentIntent(Context context) {
        Intent intent = new Intent();
        intent.setComponent(getWeatherInfoActivityComponentName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
    
    protected String formatTicker(Context context, Weather weather, TemperatureType unit) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Temperature tempC = condition.getTemperature(TemperatureUnit.C);
        Temperature tempF = condition.getTemperature(TemperatureUnit.F);
        return context.getString(ids.id(STRING, "notification_ticker"),
                weather.getLocation().getText(),
                createTemperatureFormat().format(tempC.getCurrent(), tempF.getCurrent(), unit));
    }

    protected String formatContentTitle(Context context, Weather weather, NotificationStyler styler) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        WeatherCondition condition = weather.getConditions().get(0);
        Temperature tempC = condition.getTemperature(TemperatureUnit.C);
        Temperature tempF = condition.getTemperature(TemperatureUnit.F);
        return context.getString(ids.id(STRING, "notification_content_title"),
                createTemperatureFormat().format(tempC.getCurrent(), tempF.getCurrent(), styler.getTempType()),
                createWeatherConditionFormat(context).getText(condition));
    }

    protected String formatContentText(Context context, Weather weather, NotificationStyler styler) {
        ResourceIdFactory ids = ResourceIdFactory.getInstance(context);
        WeatherCondition condition = weather.getConditions().get(0);

        TemperatureFormat tempFormat = createTemperatureFormat();
        TemperatureUnit tempUnit = styler.getTempType().getTemperatureUnit();
        Temperature temp = condition.getTemperature(tempUnit);

        Wind wind = condition.getWind(styler.getWindUnit().getWindSpeedUnit());
        Humidity humidity = condition.getHumidity();

        return context.getString(ids.id(STRING, "notification_content_text"),
                tempFormat.format(temp.getHigh()),
                tempFormat.format(temp.getLow()),
                createWindFormat(context).format(wind),
                createHumidityFormat(context).format(humidity));
    }

    protected Bitmap formatLargeIcon(Context context, Weather weather) {
        WeatherCondition condition = weather.getConditions().get(0);

        WeatherConditionFormat format = createWeatherConditionFormat(context);
        Drawable drawable = format.getDrawable(condition);
        drawable.setLevel(LARGE_ICON);
        return Drawable2Bitmap.convert(drawable);
    }
    
    protected void notifyHandler(Weather weather) {
        synchronized (BaseWeatherNotificationReceiver.class) {   //monitor of static methods
            if (handler == null) {
                return;
            }
            Message message = handler.obtainMessage();
            Bundle bundle = message.getData();
            bundle.putParcelable(WEATHER_KEY, new ParcelableWeather2(weather));
            message.sendToTarget();
        }
    }
    
    /**
     *  Returns the component name of the weather info activity
     */
    abstract protected ComponentName getWeatherInfoActivityComponentName();
    
    /**
     *  Returns the ID of the notification icon based on the current weather.
     */
    protected int getNotificationIconId(Weather weather) {
        List<WeatherCondition> conditions = weather.getConditions();
        if (conditions == null || conditions.isEmpty()) {
            return WeatherConditionFormat.getDrawableId(WeatherConditionType.CLOUDS_CLEAR);
        }
        return WeatherConditionFormat.getDrawableId(weather.getConditions().iterator().next());
    }

    /**
     *  Returns the notification icon level.
     */
    protected int getNotificationIconLevel(Weather weather, TemperatureUnit unit) {
        return 24;  //24dp for notification icon size
    };
    
    /**
     *  Creates the temperature formatter.
     */
    protected TemperatureFormat createTemperatureFormat() {
        return new TemperatureFormat();
    }
    
    /**
     *  Creates the remove view layout for the notification.
     */
    protected RemoteWeatherLayout createRemoteWeatherLayout(Context context, RemoteViews views, NotificationStyler styler) {
        return new RemoteWeatherLayout(context, views, styler);
    }

    /**
     *  Creates the notification styler for the context.
     */
    protected NotificationStyler createStyler(Context context) {
        return new NotificationStyler(context);
    }

    /**
     *  Creates the weather condition format.
     */
    protected WeatherConditionFormat createWeatherConditionFormat(Context context) {
        return new WeatherConditionFormat(context);
    }

    /**
     *  Creates the wind format.
     */
    protected WindFormat createWindFormat(Context context) {
        return new WindFormat(context);
    }

    /**
     *  Creates the humidity format.
     */
    protected HumidityFormat createHumidityFormat(Context context) {
        return new HumidityFormat(context);
    }

}
