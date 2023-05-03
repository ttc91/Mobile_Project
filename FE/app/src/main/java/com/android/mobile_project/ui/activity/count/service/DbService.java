package com.android.mobile_project.ui.activity.count.service;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public interface DbService {

    interface GetDayOfWeekHabitListByUserAndHabitIdResult{
        void onGetDayOfWeekHabitListByUserAndHabitIdSuccess(Long startTimeInMillis, CompositeDisposable disposable);
        void onGetDayOfWeekHabitListByUserAndHabitIdFailure(CompositeDisposable disposable);
    }

    interface UpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateResult{
        void onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateSuccess(CompositeDisposable disposable);
        void onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateFailure(CompositeDisposable disposable);
    }

}
