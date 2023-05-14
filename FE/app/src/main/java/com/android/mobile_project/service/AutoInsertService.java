package com.android.mobile_project.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.R;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.receiver.local.DayChangedReceiver;
import com.android.mobile_project.ui.activity.login.LoginActivity;
import com.android.mobile_project.utils.dagger.component.sub.service.AutoInsertServiceComponent;


import javax.inject.Inject;

public class AutoInsertService extends Service {

    public static final String TAG = AutoInsertService.class.getSimpleName();

    private static final String CHANNEL_ID = "insertAutoAction";

    private static final int NOTIFICATION_ID = 999;

    public static final String GROUP_KEY_NOTIFICATION = "com.android.mobile_project.group_key";

    private static final String MY_ACTION = "com.android.project.AUTO_INSERT";

    public AutoInsertServiceComponent component;

    private final IBinder binder = new AutoInsertServiceBinder();

    @Inject
    HabitInWeekDAO mHabitInWeekDAO;

    @Inject
    HabitDAO mHabitDAO;

    @Inject
    HistoryDAO mHistoryDAO;

    public static class AutoInsertServiceBinder extends Binder {
        public AutoInsertService getService(){
            return new AutoInsertService();
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        component = ((MyApplication) getApplicationContext()).provideAutoInsertServiceComponent();
        component.inject(this);
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return binder;
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void pushNotification(Context context){
        Log.i(TAG, "pushNotification");
        Intent it = new Intent(context, LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent =
                    PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent =
                    PendingIntent.getActivity(context, 0, it, 0);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("Your habit had been updated for new day. Please doing now !")
                        .setGroup(GROUP_KEY_NOTIFICATION)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void autoInsertForNewDay(Context context){
        Log.i(TAG, "autoInsertForNewDay()");
        synchronized ("doWork"){
            Log.i(TAG, "registerReceiver");
            Intent intent = new Intent(context, DayChangedReceiver.class);
            context.sendBroadcast(intent);
        }
    }

}
