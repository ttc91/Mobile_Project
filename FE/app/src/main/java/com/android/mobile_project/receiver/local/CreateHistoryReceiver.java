package com.android.mobile_project.receiver.local;

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
import com.android.mobile_project.utils.dagger.component.sub.receiver.CreateHistoryReceiverComponent;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;

public class CreateHistoryReceiver extends BroadcastReceiver {

    private static final String MY_ACTION = "com.android.project.CREATE_HISTORY";

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String VAL_NULL = "null";

    public CreateHistoryReceiverComponent component;

    @Inject
    HabitInWeekDAO mHabitInWeekDAO;

    @Inject
    HabitDAO mHabitDAO;

    @Inject
    HistoryDAO mHistoryDAO;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("CreateHistoryReceiver", "onReceive()");

        component = ((MyApplication)context.getApplicationContext()).provideCreateHistoryReceiverComponent();
        component.inject(this);

        if(MY_ACTION.equals(intent.getAction())){

            String todayFormat = LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT));
            Long dayOfWeekId = TimeUtils.getInstance().getDayOfWeekId(todayFormat);

            HabitEntity habitEntity = mHabitDAO.getFinalHabitByUserIdInBackground(DataLocalManager.getInstance().getUserId());
            HabitInWeekEntity habitInWeekEntity = mHabitInWeekDAO.getDayOfWeekHabitListByUserAndHabitIdAndId(
                    DataLocalManager.getInstance().getUserId(), habitEntity.getHabitId(), dayOfWeekId);

            if(habitInWeekEntity != null){
                HistoryEntity historyEntity = new HistoryEntity();
                historyEntity.setHabitId(habitEntity.getHabitId());
                historyEntity.setHistoryDate(todayFormat);
                historyEntity.setHistoryHabitsState(VAL_NULL);
                historyEntity.setUserId(DataLocalManager.getInstance().getUserId());
                mHistoryDAO.insertInBackground(historyEntity);
            }

        }
    }
}
