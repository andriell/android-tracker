package com.andriell.tracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.Date;

public class TrackerLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            String s = String.format("%1$,.8f %2$,.8f %3$tT", location.getLongitude(), location.getLatitude(), new Date());
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

