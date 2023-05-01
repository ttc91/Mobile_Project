package com.android.mobile_project.utils.notification;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.mobile_project.utils.time.DayOfWeek;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.RemainderModel;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    public static final String REQUEST_TAG_NAME = "habitAlarm";
    public static final String DATA_KEY_ID = "habitId";
    public static final String DATA_KEY_NAME = "habitName";
    public static final String DATA_KEY_DAYS = "habitDays";
    public static final String DATA_KEY_HOUR = "habitHour";
    public static final String DATA_KEY_MINUTE = "habitMinute";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private static boolean[] repeatDays = new boolean[]{false, false, false, false, false, false, false};

    NotificationService mService;
    boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService(). */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        boolean[] days = data.getBooleanArray(DATA_KEY_DAYS);
        java.time.DayOfWeek today = LocalDate.now().getDayOfWeek();

        if(days[today.getValue() % 7]) {
            Context context = getApplicationContext();

            long habitId = data.getLong(DATA_KEY_ID, 0L);
            String habitName = data.getString(DATA_KEY_NAME);
            /*Intent intent =
                    NotificationService.createIntent(
                            context,
                            data.getLong(DATA_KEY_ID, 0l),
                            data.getString(DATA_KEY_NAME));*/
            Intent intent = NotificationService.createIntent(
                    context,
                    habitId,
                    habitName,
                    days,
                    data.getLong(DATA_KEY_HOUR, 0L),
                    data.getLong(DATA_KEY_MINUTE, 0L));
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
            //context.startService(intent);
        }

        return Result.success();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void enqueueWorkerWithHabit(Context context, HabitModel habit, RemainderModel remainder, List<HabitInWeekModel> habitInWeekModelList) {
        loadRepeatDays(habitInWeekModelList);
        //LocalTime time = LocalTime.now();
        //time = LocalTime.of(time.getHour(), time.getMinute());
        //long numberMinusNow = time.toSecondOfDay() / 60;
        long numberMinusReminder = remainder.getHourTime() * 60 + remainder.getMinutesTime();
        /*long numberMinusDelay = 0L;
        if (numberMinusNow < numberMinusReminder) {
            numberMinusDelay = numberMinusReminder - numberMinusNow;
        } else {
            numberMinusDelay = numberMinusReminder + (1440 - numberMinusNow);
        }*/

        PeriodicWorkRequest notificationRequest =
                new PeriodicWorkRequest
                        .Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                        .addTag(REQUEST_TAG_NAME)
                        .setScheduleRequestedAt(numberMinusReminder, TimeUnit.MINUTES)
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
                REQUEST_TAG_NAME + habit.getHabitId(),
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationRequest);
    }

    private static void loadRepeatDays(List<HabitInWeekModel> habitInWeekModelList) {
        for (HabitInWeekModel habit : habitInWeekModelList){
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

    public static void cancelWorkerWithHabit(Context context, long habitId) {
        WorkManager.getInstance(context)
                .cancelUniqueWork(REQUEST_TAG_NAME + habitId);
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

