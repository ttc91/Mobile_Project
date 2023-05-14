package com.android.mobile_project.utils.worker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.mobile_project.service.AutoInsertService;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class AutoInsertWorker extends Worker {

    public static final String REQUEST_TAG_NAME = "AutoInsertWorker";

    public static final String TAG = AutoInsertWorker.class.getSimpleName();

    private AutoInsertService mService;

    private final ServiceConnection connection = new ServiceConnection() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "AutoInsertService - onServiceConnected");
            AutoInsertService.AutoInsertServiceBinder binder = (AutoInsertService.AutoInsertServiceBinder) service;
            mService = binder.getService();
            mService.autoInsertForNewDay(getApplicationContext());
            mService.pushNotification(getApplicationContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "AutoInsertService - onServiceDisconnected");
        }
    };

    public AutoInsertWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "doWork()");
        Intent it = new Intent(getApplicationContext(), AutoInsertService.class);
        getApplicationContext().bindService(it, connection, Context.BIND_AUTO_CREATE);
        return Result.success();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void enqueueWorker(Context context){
        Log.i(TAG, "enqueueWorker()");
        LocalTime time = LocalTime.now();
        time = LocalTime.of(time.getHour(), time.getMinute());
        long numberMinusNow = time.toSecondOfDay() / 60;
        long numberMinusReminder = 22 * 60 + 59;
        long numberMinusDelay = 0L;
        if (numberMinusNow < numberMinusReminder) {
            numberMinusDelay = numberMinusReminder - numberMinusNow;
        } else {
            numberMinusDelay = numberMinusReminder + (1440 - numberMinusNow);
        }
        Log.d(TAG, "enqueueAutoInsertWorker " + numberMinusDelay);

        PeriodicWorkRequest request =
                new PeriodicWorkRequest
                        .Builder(AutoInsertWorker.class, 1, TimeUnit.DAYS)
                        .addTag(REQUEST_TAG_NAME)
                        .setInitialDelay(numberMinusDelay, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                REQUEST_TAG_NAME, ExistingPeriodicWorkPolicy.REPLACE, request);

    }

    @Override
    public void onStopped() {
        Log.i(TAG, "onStopped()");
        super.onStopped();
        getApplicationContext().unbindService(connection);
    }
}
