package com.andriell.tracker;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TrackerServiceConnection connection = new TrackerServiceConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        startService(new Intent(this, TrackerService.class));
        bindService(new Intent(this, TrackerService.class), connection, Context.BIND_AUTO_CREATE);

        watchMileage();
    }

    private void watchMileage() {
        final TextView distanceView = (TextView) findViewById(R.id.distance);
        final EditText log = (EditText) findViewById(R.id.log);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0.0;
                if (connection.getService() != null) {
                    distance = connection.getService().getLocationListener().getDistance();
                    log.setText(connection.getService().getLocationListener().getStack().toString());
                }
                String distanceStr = String.format("%1$,.2f meters", distance);
                distanceView.setText(distanceStr);
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    public void onClickReset(View view) {
        connection.getService().setStart(false);
        stopService(new Intent(this, TrackerService.class));
    }
}
