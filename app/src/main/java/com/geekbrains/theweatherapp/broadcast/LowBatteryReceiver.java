package com.geekbrains.theweatherapp.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.geekbrains.theweatherapp.R;

public class LowBatteryReceiver extends BroadcastReceiver {
    private static final int messageID = 400;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if ((notificationManager == null) || (action == null)) {
            return;
        }

        if (action.equals(Intent.ACTION_BATTERY_LOW)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("The Weather App")
                    .setContentText("Battery is running low");

            notificationManager.notify(messageID, builder.build());
        } else {
            notificationManager.cancel(messageID);
        }
    }
}
