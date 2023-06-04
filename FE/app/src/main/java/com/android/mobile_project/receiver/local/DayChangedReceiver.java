package com.android.mobile_project.receiver.local;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.dao.StepHistoryDAO;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.StepHistoryEntity;
import com.android.mobile_project.service.AutoInsertService;
import com.android.mobile_project.utils.dagger.component.sub.receiver.DayChangedReceiverComponent;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

public class DayChangedReceiver extends BroadcastReceiver {

    public static final String TAG = DayChangedReceiver.class.getSimpleName();

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String VAL_NULL = "null";

    private static final String VAL_TRUE = "true";

    public DayChangedReceiverComponent component;

    private AutoInsertService mService;

    private Context mContext;

    private final ServiceConnection connection = new ServiceConnection() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "AutoInsertService - onServiceConnected");
            AutoInsertService.AutoInsertServiceBinder binder = (AutoInsertService.AutoInsertServiceBinder) service;
            mService = binder.getService();
            mService.pushNotification(mContext.getApplicationContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "AutoInsertService - onServiceDisconnected");
        }
    };

    @Inject
    HabitInWeekDAO mHabitInWeekDAO;

    @Inject
    HabitDAO mHabitDAO;

    @Inject
    HistoryDAO mHistoryDAO;

    @Inject
    StepHistoryDAO mStepHistoryDAO;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive()");

        mContext = context;

        component = ((MyApplication) mContext.getApplicationContext()).provideDayChangedReceiverComponent();
        component.inject(this);

        String todayFormat = LocalDate.now().plus(Period.ofDays(1)).format(DateTimeFormatter.ofPattern(DAY_FORMAT));
        String yesterdayFormat = LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT));
        Long dayOfWeekTodayId = TimeUtils.getInstance().getDayOfWeekId(todayFormat);
        Long dayOfWeekYesterdayId = TimeUtils.getInstance().getDayOfWeekId(yesterdayFormat);

        //update step per day
        try{
            if(DataLocalManager.getInstance().getCounterStepValue() != null){
                StepHistoryEntity entity = new StepHistoryEntity();
                entity.setUserId(1L);
                entity.setStepHistoryDate(yesterdayFormat);
                entity.setStepValue(DataLocalManager.getInstance().getCounterStepPerDayValue());
                mStepHistoryDAO.insert(entity);
                DataLocalManager.getInstance().resetCounterStepPerDayValue();
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        List<HabitInWeekEntity> habitInWeekToDayEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                1L, dayOfWeekTodayId);
        Log.i(TAG, "Habit in week today size  " + habitInWeekToDayEntities.size());

        List<HabitInWeekEntity> habitInWeekYesterdayEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                1L, dayOfWeekYesterdayId);
        Log.i(TAG, "Habit in week yesterday size " + habitInWeekYesterdayEntities.size());

        //update longest steak for planner fragment
        Long number = mHistoryDAO.countTrueStateByHistoryDateInBackground(yesterdayFormat);
        if(number > 0){
            Long steak = DataLocalManager.getInstance().getLongestTeak();
            DataLocalManager.getInstance().setLongestTeak(steak + 1L);
        }else {
            DataLocalManager.getInstance().setLongestTeak(0L);
        }

        if(habitInWeekToDayEntities.size() > 0){
            //insert history for next day
            Log.i(TAG, "Insert history for next day");
            for (HabitInWeekEntity entity : habitInWeekToDayEntities) {
                HabitEntity habitEntity = mHabitDAO.getHabitByUserIdAndHabitIdInBackground(1L, entity.getHabitId());
                HistoryEntity historyEntity = new HistoryEntity();
                historyEntity.setHabitId(habitEntity.getHabitId());
                historyEntity.setHistoryDate(todayFormat);
                historyEntity.setHistoryHabitsState(VAL_NULL);
                historyEntity.setUserId(1L);
                mHistoryDAO.insertInBackground(historyEntity);
            }
        }

        if(habitInWeekYesterdayEntities.size() > 0){
            //Update longest steak for habit
            Log.i(TAG, "Update longest steak for habit");
            for (HabitInWeekEntity entity : habitInWeekYesterdayEntities) {
                HabitEntity habitEntity = mHabitDAO.getHabitByUserIdAndHabitIdInBackground(1L, entity.getDayOfWeekId());
                try {
                    HistoryEntity historyEntity = mHistoryDAO.getHistoryByHabitIdAndDateInBackground(habitEntity.getHabitId(), yesterdayFormat);
                    if (historyEntity.getHistoryHabitsState().equals(VAL_TRUE)) {
                        habitEntity.setNumOfLongestSteak(Long.sum(habitEntity.getNumOfLongestSteak(), 1L));
                    } else {
                        habitEntity.setNumOfLongestSteak(0L);
                    }
                    mHabitDAO.updateHabitInBackground(habitEntity);
                } catch (NullPointerException e) {
                    Log.e("DayChangedReceiver", String.valueOf(e));
                }
            }
        }

        Intent it = new Intent(mContext, AutoInsertService.class);
        mContext.getApplicationContext().bindService(it, connection, Context.BIND_AUTO_CREATE);

    }


}
