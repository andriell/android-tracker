package com.andriell.tracker;

import android.os.Binder;

public class TrackerServiceBinder extends Binder {
    private TrackerService service;

    public TrackerServiceBinder(TrackerService service) {
        this.service = service;
    }

    public TrackerService getService() {
        return service;
    }
}
