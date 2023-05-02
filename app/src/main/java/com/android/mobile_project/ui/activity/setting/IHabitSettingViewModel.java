package com.android.mobile_project.ui.activity.setting;

import com.android.mobile_project.ui.activity.setting.service.DbService;


public interface IHabitSettingViewModel {

    void getHabitByUserIdAndHabitId(Long habitId, DbService.GetHabitByUserIdAndHabitIdResult callback);
    void getRemainderListByHabitId(DbService.GetRemainderListByHabitIdResult callback);
    void updateRemainder(int position, Long hourOld, Long minutesOld, Long hourNew, Long minutesNew, DbService.UpdateRemainderResult callback);
    void deleteRemainderByTimerHourAndTimerMinutesAndId(Long h, Long m, DbService.DeleteRemainderResult callback);
    void getHistoryByHabitIdAndDate(Long habitId, String date, DbService.GetHistoryByHabitAndDateResult callback);
    void deleteHabit(DbService.DeleteHabitResult callback);

}
