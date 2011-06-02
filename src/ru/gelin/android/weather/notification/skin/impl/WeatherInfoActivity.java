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

import static ru.gelin.android.weather.notification.skin.impl.SkinWeatherNotificationReceiver.WEATHER_KEY;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.MainActivity;
import ru.gelin.android.weather.notification.Tag;
import ru.gelin.android.weather.notification.UpdateService;
import ru.gelin.android.weather.notification.skin.impl.WeatherLayout;
import ru.gelin.android.weather.notification.WeatherStorage;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 *  Base class for weather info activity.
 */
public class WeatherInfoActivity extends Activity {
    
    ResourceIdFactory ids = ResourceIdFactory.getInstance(this);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(ids.id("layout", "weather_info"));
        
        final ImageButton refreshButton = (ImageButton)findViewById(ids.id("refresh_button")); 
        refreshButton.setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View v) {
                startProgress();
                UpdateService.start(WeatherInfoActivity.this, true, true);
            }
        });
        
        ImageButton preferencesButton = (ImageButton)findViewById(ids.id("preferences_button")); 
        preferencesButton.setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View v) {
                finish();
                MainActivity.start(WeatherInfoActivity.this);
            }
        });
        
        View wholeActivity = findViewById(ids.id("weather_info"));
        wholeActivity.setOnClickListener(new OnClickListener() {
            //@Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SkinWeatherNotificationReceiver.registerWeatherHandler(weatherHandler);
        WeatherStorage storage = new WeatherStorage(this);
        WeatherLayout layout = new WeatherLayout(this, findViewById(ids.id("weather_info")));
        Weather weather = storage.load();
        layout.bind(weather);
        //Location location = weather.getLocation();
        //setTitle(location == null ? "" : location.getText());
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        SkinWeatherNotificationReceiver.unregisterWeatherHandler();
    }
    
    /**
     *  Returns the PendingIntent which starts this activity.
     */
    protected static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClassName(Tag.class.getPackage().getName(), WeatherInfoActivity.class.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    final Handler weatherHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            stopProgress();
            Weather weather = (Weather)msg.getData().getParcelable(WEATHER_KEY);
            if (weather == null) {
                return;
            }
            WeatherLayout layout = new WeatherLayout(
                    WeatherInfoActivity.this, findViewById(
                            WeatherInfoActivity.this.ids.id("weather_info")));
            layout.bind(weather);
        };
    };
    
    void startProgress() {
        View refreshButton = findViewById(ids.id("refresh_button"));
        refreshButton.setEnabled(false);
        //Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        //refreshButton.startAnimation(rotate);
    }
    
    void stopProgress() {
        View refreshButton = findViewById(ids.id("refresh_button"));
        refreshButton.setEnabled(true);
    }

}
