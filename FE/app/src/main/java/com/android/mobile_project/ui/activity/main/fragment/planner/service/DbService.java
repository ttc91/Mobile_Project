package com.android.mobile_project.ui.activity.main.fragment.planner.service;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public interface DbService {

    interface GetHabitByMostLongestSteak{
        void onGetHabitByMostLongestSteakSuccess(CompositeDisposable disposable);
        void onGetHabitByMostLongestSteakFailure(CompositeDisposable disposable);
    }

    interface GetHistoryByDateResult{
        void onGetHistoryByDateResultSuccess(CompositeDisposable disposable);
        void onGetHistoryByDateResultFailure(CompositeDisposable disposable);
    }

    interface CountTrueStateByHistoryDateResult{
        void onCountTrueStateByHistoryDate(CompositeDisposable disposable);
        void onCountTrueStateByHistoryDateFailure(CompositeDisposable disposable);
    }

    interface CountHistoriesByDateResult{
        void onCountHistoriesByDateSuccess(CompositeDisposable disposable);
        void onCountHistoriesByDateFailure(CompositeDisposable disposable);
    }

}
