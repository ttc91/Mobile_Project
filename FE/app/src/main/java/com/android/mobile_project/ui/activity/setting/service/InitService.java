package com.android.mobile_project.ui.activity.setting.service;

public interface InitService {

    void getHabit();

    void getHabitInWeek();

    void getDayOfTime();

    void getRemainderList();

    void initUI();

    void initRemainderAdapter();

    void initUpdateBehavior();

    void initTimerDialog(int gravity);

    void initRemainderDialog(int gravity);

    void setCalendarOfMonthView(Long habitId);

}
