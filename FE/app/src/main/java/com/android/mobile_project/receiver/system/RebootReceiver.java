package com.android.mobile_project.receiver.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.utils.alarm.AlarmUtil;

public class RebootReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            AlarmUtil.setAutoInsertHistoryAlarm(context.getApplicationContext());
        }
    }

}
