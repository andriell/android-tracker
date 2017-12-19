package com.example.andriell.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class TrackerService extends Service {
    private final IBinder binder = new TrackerBinder();
    private final LocationListener locationListener = new TrackerLocationListener();
    private Location lastLocation = null;
    private double distance;

    public TrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    public void onDestroy() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.removeUpdates(locationListener);
    }

    public double getDistance() {
        return distance;
    }

    public class TrackerBinder extends Binder {
        TrackerService getTrackerService() {
            return TrackerService.this;
        }
    }

    public class TrackerLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (lastLocation == null) {
                lastLocation = location;
            }
            distance += location.distanceTo(lastLocation);
            lastLocation = location;
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
        }
    }
}
