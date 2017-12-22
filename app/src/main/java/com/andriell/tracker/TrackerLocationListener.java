package com.andriell.tracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Date;

public class TrackerLocationListener implements LocationListener, Serializable {
    private SizedStack<String> stack = new SizedStack(10);
    private double distance;
    private Location lastLocation = null;

    @Override
    public void onLocationChanged(Location location) {
        String s = String.format("%1$,.8f %2$,.8f %3$tT", location.getLongitude(), location.getLatitude(), new Date());
        stack.push(s);
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

    public SizedStack<String> getStack() {
        return stack;
    }

    public double getDistance() {
        return distance;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

