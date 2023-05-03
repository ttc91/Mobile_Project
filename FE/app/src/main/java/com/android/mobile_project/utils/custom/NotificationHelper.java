package com.android.mobile_project.utils.custom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.mobile_project.R;
import com.android.mobile_project.ui.activity.main.MainActivity;

public class NotificationHelper {
    private Context context;
    public static final String CHANNEL_ID = "reminder_channel_id";
    public static final int NOTIFICATION_ID = 1;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.app_name), importance);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);

        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT : PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 255, intent, flags);

        NotificationCompat.Action notifAction = new NotificationCompat.Action.Builder(R.drawable.ic_remainder,
                context.getString(R.string.app_name), pendingIntent).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setSmallIcon(R.drawable.ic_remainder)
                .setOngoing(true)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(notifAction);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /*private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Reminder Channel Description");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }*/
}
