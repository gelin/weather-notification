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

package ru.gelin.android.weather.notification.skin.builtin;

import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.WeatherLayout;
import ru.gelin.android.weather.notification.WeatherStorage;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WeatherInfoActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        WeatherStorage storage = new WeatherStorage(this);
        WeatherLayout layout = new WeatherLayout(this, findViewById(R.id.weather_info));
        Weather weather = storage.load();
        layout.bind(weather);
    }
    
    /**
     *  Returns the PendingIntent which starts this activity.
     */
    protected static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, WeatherInfoActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
