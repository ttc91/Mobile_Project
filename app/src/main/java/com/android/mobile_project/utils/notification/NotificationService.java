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

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.ui.activity.input.InputActivity;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "HabitAlarm";
    private static final String GROUP_KEY_NOTIFICATION = "com.android.mobile_project.group_key";
    private static final String INTENT_KEY_ID = "com.android.mobile_project.notification_id";
    private static final String INTENT_KEY_NAME = "com.android.mobile_project.notification_name";
    private static final String INTENT_KEY_DAYS = "com.android.mobile_project.notification_days";
    private static final String INTENT_KEY_HOUR = "com.android.mobile_project.notification_hour";
    private static final String INTENT_KEY_MINUTE = "com.android.mobile_project.notification_minute";

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void pushNotification(Intent intent) {
        long habitId = intent.getLongExtra(INTENT_KEY_ID, 0L);
        String habitName = intent.getStringExtra(INTENT_KEY_NAME);

        Intent newIntent = new Intent(this, InputActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, newIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(habitName)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setGroup(GROUP_KEY_NOTIFICATION)
                        .setAutoCancel(true)
                        // For Android 7.1 and lower
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(
                (int) habitId,
                builder.build());

    }

    public static Intent createIntent(Context packageContext, long id, String name) {
        return new Intent(packageContext, NotificationService.class)
                .putExtra(INTENT_KEY_ID, id)
                .putExtra(INTENT_KEY_NAME, name);
    }

    public static Intent createIntent(
            Context packageContext,
            long id,
            String name,
            boolean[] days,
            long hour,
            long minute
    ) {
        return new Intent(packageContext, NotificationService.class)
                .putExtra(INTENT_KEY_ID, id)
                .putExtra(INTENT_KEY_NAME, name)
                .putExtra(INTENT_KEY_DAYS, days)
                .putExtra(INTENT_KEY_HOUR, hour)
                .putExtra(INTENT_KEY_MINUTE, minute);
    }

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        NotificationService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return NotificationService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public IBinder onBind(Intent intent) {
        pushNotification(intent);
        return binder;
    }

}
