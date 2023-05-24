package com.android.mobile_project.receiver.system;

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
import com.android.mobile_project.utils.alarm.AlarmUtil;
import com.android.mobile_project.utils.dagger.component.sub.receiver.RebootReceiverComponent;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

public class RebootReceiver extends BroadcastReceiver {

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    public static final String TAG = RebootReceiver.class.getSimpleName();

    private static final String VAL_NULL = "null";

    private static final String VAL_TRUE = "true";

    @Inject
    HistoryDAO mHistoryDAO;

    @Inject
    HabitInWeekDAO mHabitInWeekDAO;

    @Inject
    HabitDAO mHabitDAO;

    public RebootReceiverComponent component;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            Log.i(TAG, "onReceive()");

            component = ((MyApplication) context.getApplicationContext()).provideRebootReceiverComponent();
            component.inject(this);

            AlarmUtil.setAutoInsertHistoryAlarm(context.getApplicationContext());

            String todayFormat = LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT));
            List<HistoryEntity> historyEntities = mHistoryDAO.getHistoriesByDateInBackground(1L, todayFormat);
            if(historyEntities.size() == 0 || historyEntities == null){

                Long dayOfWeekTodayId = TimeUtils.getInstance().getDayOfWeekId(todayFormat);
                List<HabitInWeekEntity> habitInWeekToDayEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                        1L, dayOfWeekTodayId);
                Log.i(TAG, "Habit in week today size  " + habitInWeekToDayEntities.size());

                if(habitInWeekToDayEntities.size() > 0){
                    //insert history for new day
                    Log.i(TAG, "Insert history for today");
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

                String yesterdayFormat = LocalDate.now().minus(Period.ofDays(1)).format(DateTimeFormatter.ofPattern(DAY_FORMAT));
                Long dayOfWeekYesterdayId = TimeUtils.getInstance().getDayOfWeekId(yesterdayFormat);
                List<HabitInWeekEntity> habitInWeekYesterdayEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                        1L, dayOfWeekYesterdayId);
                if(habitInWeekYesterdayEntities.size() > 0){
                    Log.i(TAG, "Habit in week yesterday size " + habitInWeekYesterdayEntities.size());

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
                            Log.e(TAG, String.valueOf(e));
                        }
                    }
                }

                //update longest steak for planner fragment
                Long number = mHistoryDAO.countTrueStateByHistoryDateInBackground(yesterdayFormat);
                if(number > 0){
                    Long steak = DataLocalManager.getInstance().getLongestTeak();
                    DataLocalManager.getInstance().setLongestTeak(steak + 1L);
                }else {
                    DataLocalManager.getInstance().setLongestTeak(0L);
                }

            }

        }
    }

}
