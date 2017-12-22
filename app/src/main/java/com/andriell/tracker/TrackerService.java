package com.andriell.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

public class TrackerService extends Service {
    private boolean start = true;
    private TrackerLocationListener locationListener = new TrackerLocationListener();

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Service created", Toast.LENGTH_SHORT).show();
        super.onCreate();
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        if (intent.hasExtra("distance")) {
            locationListener.setDistance((Double) intent.getExtras().get("distance"));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        restart();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(getApplicationContext(), "Service removed", Toast.LENGTH_SHORT).show();
        super.onTaskRemoved(rootIntent);
        restart();
    }

    private void restart() {
        if (start) {
            Intent intent = new Intent("com.andriell.tracker.restart");
            intent.putExtra("distance", locationListener.getDistance());
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
