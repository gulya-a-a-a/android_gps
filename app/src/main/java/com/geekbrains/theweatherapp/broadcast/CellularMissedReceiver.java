package com.geekbrains.theweatherapp.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.NotificationCompat;

import com.geekbrains.theweatherapp.R;

public class CellularMissedReceiver extends BroadcastReceiver {
    private final static int messageID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo info = conn.getActiveNetworkInfo();

            NotificationManager notificationManager
                    = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager == null) {
                return;
            }

            if (info != null) {
                notificationManager.cancel(messageID);
                return;
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("The Weather App")
                    .setContentText("Network was lost");

            notificationManager.notify(messageID, builder.build());
        }
    }
}
