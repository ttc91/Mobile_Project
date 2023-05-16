package com.android.mobile_project.utils.worker;


import static com.android.mobile_project.service.NotificationService.INTENT_KEY_DAYS;
import static com.android.mobile_project.service.NotificationService.INTENT_KEY_HOUR;
import static com.android.mobile_project.service.NotificationService.INTENT_KEY_ID;
import static com.android.mobile_project.service.NotificationService.INTENT_KEY_MINUTE;
import static com.android.mobile_project.service.NotificationService.INTENT_KEY_NAME;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.mobile_project.service.NotificationService;
import com.android.mobile_project.utils.time.DayOfWeek;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.RemainderModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    public static final String REQUEST_TAG_NAME = "habitAlarm";
    public static final String DATA_KEY_ID = "habitId";
    public static final String DATA_KEY_NAME = "habitName";
    public static final String DATA_KEY_DAYS = "habitDays";
    public static final String DATA_KEY_HOUR = "habitHour";
    public static final String DATA_KEY_MINUTE = "habitMinute";

    public static final String TAG = NotificationWorker.class.getSimpleName();

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private static boolean[] repeatDays = new boolean[]{false, false, false, false, false, false, false};

    NotificationService mService;
    boolean mBound = false;
    private Intent intent;

    /**
     * Defines callbacks for service binding, passed to bindService().
     */
    private ServiceConnection connection;
    private ServiceConnection createConnection() {
        return new ServiceConnection() {
            
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance.
                NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;
                Log.d(TAG, "onServiceConnected: " + className);
                mService.pushNotification(intent, getApplicationContext());
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
                Log.d(TAG, "onServiceDisconnected: " + arg0);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: " + mBound);
        Data data = getInputData();
        boolean[] days = data.getBooleanArray(DATA_KEY_DAYS);
        java.time.DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (days[today.getValue() % 7]) {
            Log.d(TAG, "doWork: " + today.getValue() % 7);
            Context context = getApplicationContext();

            long habitId = data.getLong(DATA_KEY_ID, 0L);
            String habitName = data.getString(DATA_KEY_NAME);
            intent = createIntent(
                    context,
                    habitId,
                    habitName,
                    days,
                    data.getLong(DATA_KEY_HOUR, 0L),
                    data.getLong(DATA_KEY_MINUTE, 0L));
            connection = createConnection();
            Log.d(TAG, "connection: " + connection.toString());
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }

        return Result.success();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void enqueueWorkerWithHabit(Context context, HabitModel habit, RemainderModel remainder, List<HabitInWeekModel> habitInWeekModelList) {
        if (habitInWeekModelList == null || habitInWeekModelList.size() == 0) {
            return;
        }
        loadRepeatDays(habitInWeekModelList);
        LocalTime time = LocalTime.now();
        time = LocalTime.of(time.getHour(), time.getMinute());
        long numberMinusNow = time.toSecondOfDay() / 60;
        long numberMinusReminder = remainder.getHourTime() * 60 + remainder.getMinutesTime();
        long numberMinusDelay = 0L;
        if (numberMinusNow < numberMinusReminder) {
            numberMinusDelay = numberMinusReminder - numberMinusNow;
        } else {
            numberMinusDelay = numberMinusReminder + (1440 - numberMinusNow);
        }
        Log.d(TAG, "enqueueWorkerWithHabit: " + numberMinusDelay);
        PeriodicWorkRequest notificationRequest =
                new PeriodicWorkRequest
                        .Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                        .addTag(REQUEST_TAG_NAME)
                        .setInitialDelay(numberMinusDelay, TimeUnit.MINUTES)
                        //.setScheduleRequestedAt(numberMinusReminder, TimeUnit.MINUTES)
                        .setInputData(new Data
                                .Builder()
                                .putLong(DATA_KEY_ID, habit.getHabitId())
                                .putString(DATA_KEY_NAME, habit.getHabitName())
                                .putBooleanArray(DATA_KEY_DAYS, repeatDays)
                                .putLong(DATA_KEY_HOUR, remainder.getHourTime())
                                .putLong(DATA_KEY_MINUTE, remainder.getMinutesTime())
                                .build()
                        )
                        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                REQUEST_TAG_NAME + habit.getHabitId() + remainder.getHourTime() + remainder.getMinutesTime(),
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationRequest);
    }

    private static void loadRepeatDays(List<HabitInWeekModel> habitInWeekModelList) {
        for (HabitInWeekModel habit : habitInWeekModelList) {
            if (habit.getDayOfWeekId().equals(DayOfWeek.SUN.getId())) {
                repeatDays[0] = true;
            } else if (habit.getDayOfWeekId().equals(DayOfWeek.MON.getId())) {
                repeatDays[1] = true;
            } else if (habit.getDayOfWeekId().equals(DayOfWeek.TUE.getId())) {
                repeatDays[2] = true;
            } else if (habit.getDayOfWeekId().equals(DayOfWeek.WED.getId())) {
                repeatDays[3] = true;
            } else if (habit.getDayOfWeekId().equals(DayOfWeek.THU.getId())) {
                repeatDays[4] = true;
            } else if (habit.getDayOfWeekId().equals(DayOfWeek.FRI.getId())) {
                repeatDays[5] = true;
            } else if (habit.getDayOfWeekId().equals(DayOfWeek.SAT.getId())) {
                repeatDays[6] = true;
            }
        }
    }

    public static void cancelWorkerWithHabit(Context context, long habitId, List<RemainderModel> remainderList) {
        if (remainderList != null && remainderList.size() > 0) {
            Log.d(TAG, "cancelWorkerWithHabit: " + remainderList.size());
            for (RemainderModel remainder : remainderList) {
                cancelOneWorkerWithHabit(context, habitId, remainder.getHourTime(), remainder.getMinutesTime());
            }
        }
    }

    public static void cancelOneWorkerWithHabit(Context context, long habitId, Long h, Long m) {
        Log.d(TAG, "cancelOneWorkerWithHabit: " + habitId);
        WorkManager.getInstance(context)
                .cancelUniqueWork(REQUEST_TAG_NAME + habitId + h + m);
    }

    public static void cancelAllWorkers(Context context) {
        WorkManager.getInstance(context).cancelAllWork();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        getApplicationContext().unbindService(connection);
        mBound = false;
    }
}

