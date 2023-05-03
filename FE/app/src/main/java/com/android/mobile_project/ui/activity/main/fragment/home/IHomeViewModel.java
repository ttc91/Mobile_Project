package com.android.mobile_project.ui.activity.main.fragment.home;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.ui.activity.main.fragment.home.service.DbService;

import java.util.List;

public interface IHomeViewModel {
    void getHistoryByHabitIdAndDate(Long id, String date,  DbService.GetHistoryByHabitIdAndDateResult callback);
    List<HabitInWeekModel> getDayOfWeekHabitListByUserAndHabitId(Long habitId);
}
