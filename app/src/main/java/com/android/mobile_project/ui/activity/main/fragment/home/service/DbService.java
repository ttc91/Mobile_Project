package com.android.mobile_project.ui.activity.main.fragment.home.service;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public interface DbService {

    interface GetHabitInWeekListResult{
        void onGetHabitInWeekListSuccess(int size, List<HabitInWeekModel> models, CompositeDisposable disposable);
        void onGetHabitInWeekListFailure(CompositeDisposable disposable);
    }

    interface GetHistoryByDateResult{
        void onGetHistoryByDateSuccess(CompositeDisposable disposable);
        void onGetHistoryByDateFailure(CompositeDisposable disposable);
    }

    interface InsertHistoryResult{
        void onInsertHistorySuccess(CompositeDisposable disposable);
        void onInsertHistoryFailure(CompositeDisposable disposable);
    }

    interface UpdateHistoryResult{
        void onUpdateHistorySuccess(CompositeDisposable disposable);
        void onUpdateHistoryFailure(CompositeDisposable disposable);
    }

    interface GetHistoryByHabitIdAndDateResult{
        void onGetHistoryByHabitIdAndDateSuccess(HistoryModel model);
        void onGetHistoryByHabitIdAndDateFailure();
    }

    interface GetHabitByUserIdAndHabitIdResult{
        void onGetHabitByUserIdAndHabitIdSuccess(HabitModel model, CompositeDisposable disposable);
        void onGetHabitByUserIdAndHabitIdFailure(CompositeDisposable disposable);
    }

    interface GetHabitsWhenClickDailyCalendarResult{
        void onGetHabitsWhenClickDailyCalendarSuccess(CompositeDisposable disposable);
        void onGetHabitsWhenClickDailyCalendarFailure(CompositeDisposable disposable);
    }

}
