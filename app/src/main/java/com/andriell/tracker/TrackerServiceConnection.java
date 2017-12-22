package com.andriell.tracker;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class TrackerServiceConnection implements ServiceConnection {
    private TrackerService service;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        TrackerServiceBinder binder = (TrackerServiceBinder) iBinder;
        service = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public TrackerService getService() {
        return service;
    }
}
