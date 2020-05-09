package com.geekbrains.theweatherapp.location;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class LocationService extends IntentService {
    private LocationManager mLocationManager = null;
    private LocListener mLocListener = null;

    private final static String LOG_TAG = "Location";

    public LocationService() {
        super("LocationService");
    }

    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (mLocationManager == null)
            return;

        mLocListener = new LocListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000L, 1.0F, mLocListener);
    }

    private static final class LocListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(LOG_TAG, "Location changed");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(LOG_TAG, "GPS enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(LOG_TAG, "GPS disabled");
        }
    }
}
