package com.android.mobile_project.ui.activity.create.service;

public interface DbService {

    interface InsertHabit{
        void onInsertHabitSuccess();
        void onInsertHabitFailure();
    }

    interface GetHabitByName{
        void onGetHabitByNameSuccess();
        void onGetHabitByNameFailure();
    }

    interface InsertHabitInWeek{
        void onInsertHabitInWeekSuccess();
        void onInsertHabitInWeekFailure();
    }

}
