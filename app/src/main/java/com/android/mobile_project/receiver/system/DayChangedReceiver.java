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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class DayChangedReceiver extends BroadcastReceiver {

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String VAL_NULL = "null";

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

        String todayFormat = LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT));
        Long dayOfWeekId = TimeUtils.getInstance().getDayOfWeekId(todayFormat);

        List<HabitInWeekEntity> habitInWeekEntities = mHabitInWeekDAO.getHabitInWeekEntityByDayOfWeekIdInBackground(
                DataLocalManager.getInstance().getUserId(), dayOfWeekId);

        for(HabitInWeekEntity entity : habitInWeekEntities){
            HabitEntity habitEntity = mHabitDAO.getHabitByUserIdAndHabitIdInBackground(DataLocalManager.getInstance().getUserId(), entity.getDayOfWeekId());
            HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setHabitId(habitEntity.getHabitId());
            historyEntity.setHistoryDate(todayFormat);
            historyEntity.setHistoryHabitsState(VAL_NULL);
            historyEntity.setUserId(DataLocalManager.getInstance().getUserId());
            mHistoryDAO.insertInBackground(historyEntity);
        }


    }

}
