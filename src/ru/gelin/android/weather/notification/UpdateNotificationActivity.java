package ru.gelin.android.weather.notification;

import ru.gelin.android.weather.notification.skin.WeatherNotificationManager;
import android.os.Handler;
import android.preference.PreferenceActivity;

/**
 *  Basic class which can update all weather notifications.
 */
public class UpdateNotificationActivity extends PreferenceActivity {

    /** Handler to take notification update actions */
    Handler handler = new Handler();

    /**
     *  Performs the deferred update of the notification,
     *  which allows to return from onPreferenceChange handler to update
     *  preference value and update the notification later.
     */
    protected void updateNotification() {
        handler.post(new Runnable() {
            //@Override
            public void run() {
                WeatherNotificationManager.update(UpdateNotificationActivity.this);
            }
        });
    }

}
