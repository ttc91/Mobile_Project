package com.android.mobile_project.ui.activity.setting.service;

public interface ToastService {

    void makeUpdateHabitCompleteToast();
    void makeErrorApplicationToast();
    void makeDaysOfWeekInputtedIsEmptyToast();
    void makeDayOfTimeInputtedIsEmptyToast();
    void makeHabitNameIsExistedToast();
    void makeHabitNameInputtedIsEmptyToast();
    void makeRemainderWasExistedToast();


    interface HabitSettingConstant{
        String CONTENT_HABIT_UPDATE_COMPLETE = "Habit was updated !";
        String CONTENT_HABIT_NAME_IS_EMPTY = "Habit name is empty";
        String CONTENT_HABIT_NAME_IS_EXISTED = "Habit has been existed, please input other name of habit";
        String CONTENT_ERROR = "System has some error now, please contract to us";
        String CONTENT_DAY_OF_WEEK_IS_EMPTY = "Please choose your days of week !";
        String CONTENT_DAY_OF_TIME_IS_EMPTY = "Please choose your day time !";
        String CONTENT_REMAINDER_WAS_EXISTED = "Your remainder was existed !";
    }

}
