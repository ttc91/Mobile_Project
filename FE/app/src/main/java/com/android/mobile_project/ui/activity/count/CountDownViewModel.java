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
import com.android.mobile_project.ui.activity.base.BaseViewModel;
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

    private HabitModel habitModel;

    private float percent = 0;

    private Long startTimeInMillis;

    protected InitService initService;
    protected TimerService timerService;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning = false;

    public HabitModel getHabitModel() {
        return habitModel;
    }

    public void setHabitModel(HabitModel habitModel) {
        this.habitModel = habitModel;
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

    protected HabitModel getHabitByUserIdAndHabitId(Long habitId){

        final HabitModel[] habitModels = {new HabitModel()};

        mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<HabitEntity>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("getHabitByUserIdAndHabitId","onSubscribe");
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(@NonNull HabitEntity habitEntity) {
                        Log.i("getHabitByUserIdAndHabitId","pnSuccess");
                        habitModels[0] = HabitMapper.getInstance().mapToModel(habitEntity);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getHabitByUserIdAndHabitId", "onError", e);
                    }
                });
        return habitModels[0];
    }

    public List<HabitInWeekModel> getDayOfWeekHabitListByUserAndHabitId(Long habitId){

        final List<HabitInWeekModel>[] habitInWeekModels = new List[]{new ArrayList<>()};

        mHabitInWeekRepository.getMHabitInWeekDataSource().getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                .observeOn(Schedulers.single())
                .subscribeWith(new DisposableSubscriber<List<HabitInWeekEntity>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(List<HabitInWeekEntity> habitInWeekEntities) {
                        habitInWeekModels[0] = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable t) {
                        Log.e("getDayOfWeekHabitListByUserAndHabitId", "onError", t);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete() {
                        Log.i("getDayOfWeekHabitListByUserAndHabitId","onComplete");
                    }
                });

        return habitInWeekModels[0];
    }

    public HistoryModel getHistoryByHabitIdAndDate(Long habitId, String date){

        final HistoryModel[] historyModels = {new HistoryModel()};

        mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(habitId, date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<HistoryEntity>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("getHistoryByHabitIdAndDate", "onSubscribe");
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(@NonNull HistoryEntity historyEntity) {
                        Log.i("getHistoryByHabitIdAndDate", "onSuccess");
                        historyModels[0] = HistoryMapper.getInstance().mapToModel(historyEntity);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getHistoryByHabitIdAndDate", "onError", e);

                    }
                });
        return historyModels[0];
    }

    protected void updateHistory(HistoryModel historyModel){
        mHistoryRepository.getMHistoryDataSource().update(HistoryMapper.getInstance().mapToEntity(historyModel))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("updateHistory", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("updateHistory", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("updateHistory", "onError", e);
                    }
                });
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
