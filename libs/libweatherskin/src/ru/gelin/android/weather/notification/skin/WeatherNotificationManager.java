/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
 */

package ru.gelin.android.weather.notification.skin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.IntentParameters;
import ru.gelin.android.weather.notification.ParcelableWeather;
import ru.gelin.android.weather.notification.ParcelableWeather2;
import ru.gelin.android.weather.notification.WeatherStorage;

import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION;
import static ru.gelin.android.weather.notification.PreferenceKeys.ENABLE_NOTIFICATION_DEFAULT;

/**
 *  Managers the broadcast receivers to receive the weather notification.
 */
@SuppressWarnings("deprecation")
public class WeatherNotificationManager {
    
    Context context;
    SkinManager sm;
    
    /**
     *  Updates the notification.
     */
    public static void update(Context context) {
        WeatherNotificationManager manager = new WeatherNotificationManager(context);
        if (!manager.isEnabled()) {
            manager.cancelAll();
            return;
        }
        manager.cancelDisabled();
        manager.sendWeather();
    }
    
    WeatherNotificationManager(Context context) {
        this.context = context;
        this.sm = new SkinManager(context);
    }

    /**
     *  Returns true if the notification is enabled.
     */
    boolean isEnabled() {
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(this.context);
        return prefs.getBoolean(ENABLE_NOTIFICATION, ENABLE_NOTIFICATION_DEFAULT);
    }
    
    /**
     *  Cancels the notification
     */
    void cancelAll() {
        Intent intent = new Intent(IntentParameters.ACTION_WEATHER_UPDATE);  //cancel sends to all
        intent.putExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false);
        this.context.sendBroadcast(intent);
        Intent intent2 = new Intent(IntentParameters.ACTION_WEATHER_UPDATE_2);
        intent2.putExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, false);
        this.context.sendBroadcast(intent2);
    }
    
    /**
     *  Cancels notification for the disabled skins
     */
    void cancelDisabled() {
        for (SkinInfo skin : this.sm.getDisabledSkins()) {
            Intent intent = createIntent(skin, false, null);
            intent.setClassName(skin.getPackageName(), skin.getBroadcastReceiverClass());
            context.sendBroadcast(intent);
        }
    }
    
    /**
     *  Notifies with new weather value.
     */
    void sendWeather() {
        WeatherStorage storage = new WeatherStorage(this.context);
        Weather weather = storage.load();
        
        for (SkinInfo skin : this.sm.getEnabledSkins()) {
            Intent intent = createIntent(skin, true, weather);
            intent.setClassName(skin.getPackageName(), skin.getBroadcastReceiverClass());
            context.sendBroadcast(intent);
        }
    }
    
    /**
     *  Creates the intents with the weather.
     *  Returns two intents with different Action and Weather object type for backward compatibility.
     */
    static Intent createIntent(SkinInfo skin, boolean enableNotification, Weather weather) {
        switch (skin.getVersion()) {
        case V1:
            return createIntent(enableNotification, weather);
        case V2:
            return createIntent2(enableNotification, weather);
        }
        throw new RuntimeException("unknown skin version");
    }

    static Intent createIntent(boolean enableNotification, Weather weather) {
        Intent intent = new Intent(IntentParameters.ACTION_WEATHER_UPDATE);
        intent.putExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, enableNotification);
        if (enableNotification) {
            ParcelableWeather oldParcel = new ParcelableWeather(weather);
            intent.putExtra(IntentParameters.EXTRA_WEATHER, oldParcel);
        }
        return intent;
    }

    static Intent createIntent2(boolean enableNotification, Weather weather) {
        Intent intent = new Intent(IntentParameters.ACTION_WEATHER_UPDATE_2);
        intent.putExtra(IntentParameters.EXTRA_ENABLE_NOTIFICATION, enableNotification);
        if (enableNotification) {
            ParcelableWeather2 parcel = new ParcelableWeather2(weather);
            intent.putExtra(IntentParameters.EXTRA_WEATHER, parcel);
        }
        return intent;
    }

}
