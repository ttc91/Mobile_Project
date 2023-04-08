package com.android.mobile_project.ui.activity.setting.service;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public interface DbService {

    interface GetHabitByUserIdAndHabitIdResult{
        void onGetHabitByUserIdAndHabitIdSuccess(HabitModel model, CompositeDisposable disposable);
        void onGetHabitByUserIdAndHabitIdFailure(CompositeDisposable disposable);
    }

    interface GetHabitInWeekHabitListByUserAndHabitId{
        void onGetHabitInWeekHabitListByUserAndHabitIdSuccess(CompositeDisposable disposable);
        void onGetHabitInWeekHabitListByUserAndHabitIdFailure(CompositeDisposable disposable);
    }

    interface DeleteHabitInWeekResult{
        void onDeleteHabitInWeekSuccess(CompositeDisposable disposable);
        void onDeleteHabitInWeekFailure(CompositeDisposable disposable);
    }

    interface InsertHabitInWeekResult{
        void onInsertHabitInWeekSuccess(CompositeDisposable disposable);
        void onInsertHabitInWeekFailure(CompositeDisposable disposable);
    }

    interface UpdateDateOfTimeInHabitResult{
        void onUpdateDateOfTimeInHabitSuccess(CompositeDisposable disposable);
        void onUpdateDateOfTimeInHabitFailure(CompositeDisposable disposable);
    }

    interface GetHabitByNameResult{
        void onGetHabitByNameSuccess(CompositeDisposable disposable, Long habitId);
        void onGetHabitByNameFailure(CompositeDisposable disposable);
    }

    interface UpdateNameOfHabitResult{
        void onUpdateNameOfHabitSuccess(CompositeDisposable disposable);
        void onUpdateNameOfHabitFailure(CompositeDisposable disposable);
    }

    interface CheckExistRemainderResult{
        void onCheckExistRemainderSuccess(Long remainderId, CompositeDisposable disposable);
        void onCheckExistRemainderFailure(CompositeDisposable disposable);
    }

    interface InsertRemainderResult{
        void onInsertRemainderSuccess(CompositeDisposable disposable);
        void onInsertRemainderFailure(CompositeDisposable disposable);
    }

    interface DeleteRemainderResult{
        void onDeleteRemainderSuccess(CompositeDisposable disposable);
        void onDeleteRemainderFailure(CompositeDisposable disposable);
    }

    interface GetRemainderListByHabitIdResult{
        void onGetRemainderListByHabitIdSuccess(CompositeDisposable disposable);
        void onGetRemainderListByHabitIdFailurẹ(CompositeDisposable disposable);
    }

    interface UpdateRemainderResult{
        void onUpdateRemainderSuccess(CompositeDisposable disposable);
        void onUpdateRemainderFailurẹ(CompositeDisposable disposable);
    }

    interface GetHistoryByHabitAndDateResult{
        void onGetHistoryByHabitAndDateSuccess(HistoryModel model, CompositeDisposable disposable);
        void onGetHistoryByHabitAndDateFailure(CompositeDisposable disposable);
    }

    interface DeleteHabitResult{
        void onDeleteHabitSuccess(CompositeDisposable disposable);
        void onDeleteHabitFailure(CompositeDisposable disposable);
    }

}
