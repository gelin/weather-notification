package ru.gelin.android.weather.notification.skin.builtin;

import static ru.gelin.android.weather.notification.Tag.TAG;
import ru.gelin.android.weather.Weather;
import ru.gelin.android.weather.notification.R;
import ru.gelin.android.weather.notification.WeatherLayout;
import ru.gelin.android.weather.notification.WeatherStorage;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WeatherInfoActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
    }
    
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
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
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
