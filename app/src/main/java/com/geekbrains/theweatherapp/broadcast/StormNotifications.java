package com.geekbrains.theweatherapp.broadcast;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class StormNotifications extends FirebaseMessagingService {
    private static final String LOG_TAG = "Storm Notification";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            RemoteMessage.Notification not = remoteMessage.getNotification();
            if (not != null) {
                Log.d(LOG_TAG, not.getBody());
            } else {
                Log.e(LOG_TAG, "Empty message");
            }
        } catch (NullPointerException ex) {
            Log.e(LOG_TAG, "Empty message");
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(LOG_TAG, String.format("New token: %s", s));
    }
}
