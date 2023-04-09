package com.android.mobile_project.receiver.system;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.utils.dagger.component.sub.receiver.DayChangedReceiverComponent;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

public class DayChangedReceiver extends BroadcastReceiver {

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String VAL_NULL = "null";

    private static final String VAL_TRUE = "true";

    private static final String DAY_TO_UPDATE = "23:59";

    public DayChangedReceiverComponent component;

    @Inject
    HabitInWeekDAO mHabitInWeekDAO;

    @Inject
    HabitDAO mHabitDAO;

    @Inject
    HistoryDAO mHistoryDAO;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("DayChangedReceiver", "onReceive()");

        component = ((MyApplication) context.getApplicationContext()).provideDayChangedReceiverComponent();
        component.inject(this);

        if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            LocalTime localTime = LocalTime.now();
            if((localTime.getHour()+":"+localTime.getMinute()).equals(DAY_TO_UPDATE)){
                String todayFormat = LocalDate.now().plus(Period.ofDays(1)).format(DateTimeFormatter.ofPattern(DAY_FORMAT));
                String yesterdayFormat = LocalDate.now().minus(Period.ofDays(1)).format(DateTimeFormatter.ofPattern(DAY_FORMAT));
                Long dayOfWeekTodayId = TimeUtils.getInstance().getDayOfWeekId(todayFormat);
                Long dayOfWeekYesterdayId = TimeUtils.getInstance().getDayOfWeekId(yesterdayFormat);

                List<HabitInWeekEntity> habitInWeekToDayEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                        DataLocalManager.getInstance().getUserId(), dayOfWeekTodayId);

                List<HabitInWeekEntity> habitInWeekYesterdayEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                        DataLocalManager.getInstance().getUserId(), dayOfWeekYesterdayId);

                //insert history for next day
                for(HabitInWeekEntity entity : habitInWeekToDayEntities){
                    HabitEntity habitEntity = mHabitDAO.getHabitByUserIdAndHabitIdInBackground(DataLocalManager.getInstance().getUserId(), entity.getDayOfWeekId());
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.setHabitId(habitEntity.getHabitId());
                    historyEntity.setHistoryDate(todayFormat);
                    historyEntity.setHistoryHabitsState(VAL_NULL);
                    historyEntity.setUserId(DataLocalManager.getInstance().getUserId());
                    mHistoryDAO.insertInBackground(historyEntity);
                }

                //Update longest steak for habit
                for(HabitInWeekEntity entity : habitInWeekYesterdayEntities){
                    HabitEntity habitEntity = mHabitDAO.getHabitByUserIdAndHabitIdInBackground(DataLocalManager.getInstance().getUserId(), entity.getDayOfWeekId());
                    try {
                        HistoryEntity historyEntity = mHistoryDAO.getHistoryByHabitIdAndDateInBackground(habitEntity.getHabitId(), yesterdayFormat);
                        if(historyEntity.getHistoryHabitsState().equals(VAL_TRUE)){
                            habitEntity.setNumOfLongestSteak(Long.sum(habitEntity.getNumOfLongestSteak(), 1L));
                        }else {
                            habitEntity.setNumOfLongestSteak(0L);
                        }
                        mHabitDAO.updateHabitInBackground(habitEntity);
                    }catch (NullPointerException ignored){
                        Log.e("DayChangedReceiver", String.valueOf(ignored));
                    }
                }
            }

        }

    }

}
