package com.andriell.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

public class TrackerService extends Service {
    private boolean start = true;
    private TrackerLocationListener locationListener = new TrackerLocationListener();

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        if (start) {
            Intent intent = new Intent("com.andriell.tracker.restart");
            intent.putExtra("locationListener", locationListener);
            sendBroadcast(intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TrackerServiceBinder(this);
    }

    public TrackerLocationListener getLocationListener() {
        return locationListener;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
