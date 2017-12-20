package com.andriell.tracker;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TrackerService trackerService;
    private boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            TrackerService.TrackerBinder odometerBinder = (TrackerService.TrackerBinder) binder;
            trackerService = odometerBinder.getTrackerService();
            bound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    public void onClickReset(View view) {
        if (trackerService == null) {
            return;
        }
        trackerService.setDistance(0d);
    }

    private void watchMileage() {
        final TextView distanceView = (TextView) findViewById(R.id.distance);
        final EditText log = (EditText) findViewById(R.id.log);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0.0;
                if (trackerService != null) {
                    distance = trackerService.getDistance();
                    log.setText(trackerService.getStack().toString());
                }
                String distanceStr = String.format("%1$,.2f meters", distance);
                distanceView.setText(distanceStr);
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        watchMileage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TrackerService.class));
        bindService(new Intent(this, TrackerService.class), connection, Context.BIND_AUTO_CREATE);
    }

    public void onClickStart(View v) {
        startService(new Intent(this, TrackerService.class));
    }

    public void onClickStop(View v) {
        stopService(new Intent(this, TrackerService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }
}