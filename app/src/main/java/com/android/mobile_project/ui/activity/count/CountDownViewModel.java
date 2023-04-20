package com.android.mobile_project.ui.activity.count;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.ui.activity.count.service.DbService;
import com.android.mobile_project.ui.activity.count.service.InitService;
import com.android.mobile_project.ui.activity.count.service.TimerService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@MyCustomAnnotation.MyScope.ActivityScope
public class CountDownViewModel extends BaseViewModel {

    private static final String TAG = CountDownViewModel.class.getSimpleName();

    private final HabitRepository mHabitRepository;
    private final HabitInWeekRepository mHabitInWeekRepository;
    private final HistoryRepository mHistoryRepository;

    @Inject
    public CountDownViewModel(HabitRepository mHabitRepository,
                              HabitInWeekRepository mHabitInWeekRepository,
                              HistoryRepository mHistoryRepository) {
        this.mHabitRepository = mHabitRepository;
        this.mHabitInWeekRepository = mHabitInWeekRepository;
        this.mHistoryRepository = mHistoryRepository;
    }

    private Long habitId;

    private float percent = 0;

    private Long startTimeInMillis;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected InitService initService;
    protected TimerService timerService;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning = false;

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public Long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public void setStartTimeInMillis(Long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;
    }

    public CountDownTimer getMCountDownTimer() {
        return mCountDownTimer;
    }

    public void setMCountDownTimer(CountDownTimer mCountDownTimer) {
        this.mCountDownTimer = mCountDownTimer;
    }

    public boolean isMTimerRunning() {
        return mTimerRunning;
    }

    public void setMTimerRunning(boolean mTimerRunning) {
        this.mTimerRunning = mTimerRunning;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    public void getDayOfWeekHabitListByUserAndHabitId(Long habitId, DbService.GetDayOfWeekHabitListByUserAndHabitIdResult callback){

       mCompositeDisposable.add(
               mHabitInWeekRepository.getMHabitInWeekDataSource().getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                       .map(habitInWeekEntities -> HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities))
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribeOn(Schedulers.io())
                       .subscribe(habitInWeekModels -> {
                           Log.i("getDayOfWeekHabitListByUserAndHabitId", "onNext");
                           setStartTimeInMillis(TimeUnit.HOURS.toMillis(habitInWeekModels.get(0).getTimerHour())
                                   + TimeUnit.MINUTES.toMillis(habitInWeekModels.get(0).getTimerMinute())
                                   + TimeUnit.SECONDS.toMillis(habitInWeekModels.get(0).getTimerSecond()));
                           callback.onGetDayOfWeekHabitListByUserAndHabitIdSuccess(startTimeInMillis, mCompositeDisposable);
                       }, throwable -> {
                           Log.e("getDayOfWeekHabitListByUserAndHabitId", "onError", throwable);
                           callback.onGetDayOfWeekHabitListByUserAndHabitIdFailure(mCompositeDisposable);
                       }
               )
       );

    }

    @SuppressLint("LongLogTag")
    public void updateHistoryStatusTrueWithUserIdAndHabitIdAndDate(Long habitId, String date, DbService.UpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateResult callback){

        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource()
                        .updateHistoryStatusTrueWithUserIdAndHabitIdAndDate(DataLocalManager.getInstance().getUserId(), habitId, date)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("updateHistoryStatusTrueWithUserIdAndHabitIdAndDate", "onSuccess");
                            callback.onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateSuccess(mCompositeDisposable);
                        },throwable -> {
                            Log.e("updateHistoryStatusTrueWithUserIdAndHabitIdAndDate", "onError", throwable);
                            callback.onUpdateHistoryStatusTrueWithUserIdAndHabitIdAndDateFailure(mCompositeDisposable);
                        }
                )
        );

    }

    /**
     * Cập nhật trạng thái History
     */
    public void updateHistoryStatus(Long id, String date, String value) {
        mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(id, date)
                .subscribe(new CustomSingleObserver<HistoryEntity>() {
                    @Override
                    public void onSuccess(@NonNull HistoryEntity historyEntity) {
                        HistoryModel model = HistoryMapper.getInstance().mapToModel(historyEntity);
                        model.setHistoryHabitsState(value);
                        mHistoryRepository.getMHistoryDataSource().update(HistoryMapper.getInstance().mapToEntity(model))
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.i(TAG, "updateHistory onComplete");
                                        mLiveDataIsSuccess.postValue(true);
                                    }
                                });
                    }
                });
    }

}
