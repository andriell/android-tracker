package com.andriell.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops", "Ohhhhhhh");
        Intent intentNew = new Intent(context, TrackerService.class);
        intentNew.putExtras(intent);
        context.startService(intentNew);
    }
}
