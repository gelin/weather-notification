package ru.gelin.android.weather.notification;

import static ru.gelin.android.weather.notification.Tag.TAG;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 *  Broadcast receiver which receives event about changes of network connectivity.
 *  Starts UpdateService if the network goes up.
 */
public class NetworkConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        boolean noConnection = 
            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (noConnection) {
            return;
        }
        NetworkInfo info = 
            (NetworkInfo)intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (info == null) {
            return;
        }
        if (!info.isAvailable()) {
            return;
        }
        Log.d(TAG, "network is up");
        UpdateService.start(context);
    }

}
