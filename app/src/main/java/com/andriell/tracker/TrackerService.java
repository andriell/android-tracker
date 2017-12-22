package com.andriell.tracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TrackerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 2000, 2 * 1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private Timer mTimer;

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            Log.e("Log", "Running");
        }
    };

    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.android.techtrainner");
        //intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TrackerServiceBinder(this);
    }
}
