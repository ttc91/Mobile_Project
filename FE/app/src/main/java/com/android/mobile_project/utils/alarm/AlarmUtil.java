package com.android.mobile_project.utils.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.receiver.local.DayChangedReceiver;

import java.util.Calendar;

public class AlarmUtil {

    private static final String TAG = AlarmUtil.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setAutoInsertHistoryAlarm(Context context){

        Log.i(TAG, "setAutoInsertHistoryAlarm");

        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DayChangedReceiver.class);
        PendingIntent mPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }else{
            mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 0);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, mPendingIntent);

    }

}
