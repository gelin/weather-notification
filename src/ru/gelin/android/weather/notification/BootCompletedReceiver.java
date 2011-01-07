package ru.gelin.android.weather.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *  Broadcast receiver which receives event about boot complete.
 *  Starts UpdateService.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        UpdateService.start(context);
    }

}
