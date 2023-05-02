package com.android.mobile_project.ui.activity.main.fragment.home.service;

public interface InitUIService {

    void initDailyCalendar();
    void initHistoryList();
    void initHistoryListOfDay();
    void initHabitInWeek();
    void initAdapter();

    interface InitHabitListUI{
        void initHabitModelList();
        void initHabitDoneModeList();
        void initHabitFailedModelList();
        void initHabitBeforeModelList();
        void initHabitAfterModelList();
    }

}
