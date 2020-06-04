package com.smartclick.custompushnotifications.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smartclick.custompushnotifications.receivers.MySimpleReceiver;
import com.smartclick.custompushnotifications.services.MySimpleService;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MySimpleService.class);
        i.putExtra("foo", "alarm!!");
        i.putExtra("receiver", MySimpleReceiver.setupServiceReceiver(context));
        MySimpleService.enqueueWork(context, i);
    }
}
