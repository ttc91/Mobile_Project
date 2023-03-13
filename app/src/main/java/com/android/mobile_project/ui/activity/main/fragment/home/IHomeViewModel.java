package com.android.mobile_project.ui.activity.main.fragment.home;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HistoryModel;

import java.util.List;

public interface IHomeViewModel {
    HistoryModel getHistoryByHabitIdAndDate(Long habitId, String date);
    List<HabitInWeekModel> getDayOfWeekHabitListByUserAndHabitId(Long habitId);
}
