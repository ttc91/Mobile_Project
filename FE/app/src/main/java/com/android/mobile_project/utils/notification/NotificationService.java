package com.android.mobile_project.utils.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.ui.activity.input.InputActivity;

public class NotificationService extends Service {

    public static final String TAG = NotificationService.class.getSimpleName();
    private static final String CHANNEL_ID = "HabitAlarm";
    public static final String GROUP_KEY_NOTIFICATION = "com.android.mobile_project.group_key";
    public static final String INTENT_KEY_ID = "com.android.mobile_project.notification_id";
    public static final String INTENT_KEY_NAME = "com.android.mobile_project.notification_name";
    public static final String INTENT_KEY_DAYS = "com.android.mobile_project.notification_days";
    public static final String INTENT_KEY_HOUR = "com.android.mobile_project.notification_hour";
    public static final String INTENT_KEY_MINUTE = "com.android.mobile_project.notification_minute";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void pushNotification(Intent intent, Context context) {
        Log.d(TAG, "pushNotification: ");
        long habitId = intent.getLongExtra(INTENT_KEY_ID, 0L);
        String habitName = intent.getStringExtra(INTENT_KEY_NAME);

        Intent newIntent = new Intent(context, InputActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, newIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(habitName)
                        .setContentIntent(pendingIntent)
                        .setGroup(GROUP_KEY_NOTIFICATION)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        notificationManager.notify(
                (int) habitId,
                builder.build());
    }

    private final IBinder binder = new LocalBinder();

    public static class LocalBinder extends Binder {
        NotificationService getService() {
            return new NotificationService();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        //pushNotification(intent);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        stopService(intent);
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
