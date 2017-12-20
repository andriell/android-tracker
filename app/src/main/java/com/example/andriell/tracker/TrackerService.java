package com.example.andriell.tracker;

import android.app.Notification;
import android.app.PendingIntent;
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
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackerService extends Service {
    private static final int DEFAULT_NOTIFICATION_ID = 101;

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
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification("Tracker service started", "Tracker", "Started");
        return Service.START_STICKY;
    }

    //Send custom notification
    private void sendNotification(String Ticker,String Title,String Text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                .setOngoing(true)   //Can't be swiped out
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.large))   // большая картинка
                .setTicker(Ticker)
                .setContentTitle(Title) //Заголовок
                .setContentText(Text) // Текст уведомления
                .setWhen(System.currentTimeMillis());

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT<=15) {
            notification = builder.getNotification(); // API-15 and lower
        }else{
            notification = builder.build();
        }

        startForeground(DEFAULT_NOTIFICATION_ID, notification);
    }

    private void delay() {
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
