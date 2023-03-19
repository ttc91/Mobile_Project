package com.android.mobile_project.ui.activity.create.service;

public interface ToastService {

    void makeInsertHabitSuccessToast();
    void makeHabitNameIsExistedToast();
    void makeHabitNameInputtedIsEmptyToast();
    void makeDaysOfWeekInputtedIsEmptyToast();
    void makeDayOfTimeInputtedIsEmptyToast();
    void makeErrorToast();

    interface CreateHabitToastConstant{
        String CONTENT_CREATE_HABIT_SUCCESS = "Habit is created completely !";
        String CONTENT_HABIT_NAME_IS_EMPTY = "Habit name is empty";
        String CONTENT_HABIT_NAME_IS_EXISTED = "Habit has been created, please input other name of habit";
        String CONTENT_ERROR = "System has some error now, please contract to us";
        String CONTENT_DAY_OF_WEEK_IS_EMPTY = "Please choose your days of week !";
        String CONTENT_DAY_OF_TIME_IS_EMPTY = "Please choose your day time !";
    }

}
