package com.android.mobile_project.ui.activity.setting;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;

import java.util.List;

public interface IHabitSettingViewModel {

    HabitModel getHabitByUserIdAndHabitId(Long habitId);
    List<RemainderModel> getRemainderListByHabitId();
    void updateRemainder(RemainderModel remainderModel);
    void deleteRemainder(RemainderModel remainderModel);
    HistoryModel getHistoryByHabitIdAndDate(Long habitId, String date);

}
