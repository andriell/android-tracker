package com.example.andriell.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackerService extends Service {
    private final IBinder binder = new TrackerBinder();
    private final LocationListener locationListener = new TrackerLocationListener();
    private Location lastLocation = null;
    private double distance;
    private SizedStack<String> stack = new SizedStack(10);
    private final Handler handler = new Handler();

    public TrackerService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        stack.push("onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        stack.push("onCreate");
        delay();
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    public void onDestroy() {
        stack.push("onDestroy");
        //LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locManager.removeUpdates(locationListener);
    }

    public void delay() {
        handler.post(new Runnable() {
            public void run() {
                stack.push(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                handler.postDelayed(this, 1000);
            }
        });
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public SizedStack<String> getStack() {
        return stack;
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
