package com.android.mobile_project.ui.activity.main.fragment.planner;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.ui.activity.main.fragment.planner.service.DbService;
import com.android.mobile_project.ui.activity.main.fragment.planner.service.InitService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@MyCustomAnnotation.MyScope.FragmentScope
public class PlannerViewModel extends ViewModel {

    private final HistoryRepository mHistoryRepository;
    private final HabitRepository mHabitRepository;

    @Inject
    public PlannerViewModel(HistoryRepository mHistoryRepository, HabitRepository mHabitRepository) {
        this.mHistoryRepository = mHistoryRepository;
        this.mHabitRepository = mHabitRepository;
    }

    private MutableLiveData<HabitModel> longestSteakHabitModelMutable = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistoryMondayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistoryTuesdayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistoryWednesdayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistoryThursdayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistoryFridayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistorySaturdayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Float> percentHistorySundayMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> longestSteakForPlannerFragmentMutableLiveData = new MutableLiveData<>();

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected InitService initService;

    protected static Integer steak = 0;

    public MutableLiveData<Integer> getLongestSteakForPlannerFragmentMutableLiveData() {
        return longestSteakForPlannerFragmentMutableLiveData;
    }

    public void setLongestSteakForPlannerFragmentMutableLiveData(MutableLiveData<Integer> longestSteakForPlannerFragmentMutableLiveData) {
        this.longestSteakForPlannerFragmentMutableLiveData = longestSteakForPlannerFragmentMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistoryMondayMutableLiveData() {
        return percentHistoryMondayMutableLiveData;
    }

    public void setPercentHistoryMondayMutableLiveData(MutableLiveData<Float> percentHistoryMondayMutableLiveData) {
        this.percentHistoryMondayMutableLiveData = percentHistoryMondayMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistoryTuesdayMutableLiveData() {
        return percentHistoryTuesdayMutableLiveData;
    }

    public void setPercentHistoryTuesdayMutableLiveData(MutableLiveData<Float> percentHistoryTuesdayMutableLiveData) {
        this.percentHistoryTuesdayMutableLiveData = percentHistoryTuesdayMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistoryWednesdayMutableLiveData() {
        return percentHistoryWednesdayMutableLiveData;
    }

    public void setPercentHistoryWednesdayMutableLiveData(MutableLiveData<Float> percentHistoryWednesdayMutableLiveData) {
        this.percentHistoryWednesdayMutableLiveData = percentHistoryWednesdayMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistoryThursdayMutableLiveData() {
        return percentHistoryThursdayMutableLiveData;
    }

    public void setPercentHistoryThursdayMutableLiveData(MutableLiveData<Float> percentHistoryThursdayMutableLiveData) {
        this.percentHistoryThursdayMutableLiveData = percentHistoryThursdayMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistoryFridayMutableLiveData() {
        return percentHistoryFridayMutableLiveData;
    }

    public void setPercentHistoryFridayMutableLiveData(MutableLiveData<Float> percentHistoryFridayMutableLiveData) {
        this.percentHistoryFridayMutableLiveData = percentHistoryFridayMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistorySaturdayMutableLiveData() {
        return percentHistorySaturdayMutableLiveData;
    }

    public void setPercentHistorySaturdayMutableLiveData(MutableLiveData<Float> percentHistorySaturdayMutableLiveData) {
        this.percentHistorySaturdayMutableLiveData = percentHistorySaturdayMutableLiveData;
    }

    public MutableLiveData<Float> getPercentHistorySundayMutableLiveData() {
        return percentHistorySundayMutableLiveData;
    }

    public void setPercentHistorySundayMutableLiveData(MutableLiveData<Float> percentHistorySundayMutableLiveData) {
        this.percentHistorySundayMutableLiveData = percentHistorySundayMutableLiveData;
    }

    public MutableLiveData<HabitModel> getLongestSteakHabitModelMutable() {
        return longestSteakHabitModelMutable;
    }

    public void setLongestSteakHabitModelMutable(MutableLiveData<HabitModel> longestSteakHabitModelMutable) {
        this.longestSteakHabitModelMutable = longestSteakHabitModelMutable;
    }

    protected void setLongestSteakForPlannerFragment(String historyDate){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryWithSteak", "onSuccess");
                                    Log.i("historySteak", String.valueOf(number));
                                    if(number > 0){
                                        steak += 1;
                                    }else {
                                        steak = 0;
                                    }
                                    longestSteakForPlannerFragmentMutableLiveData.postValue(steak);
                                }, throwable -> Log.e("getHistoryByDate", "onError", throwable)
                        )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryByMonday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                            Log.i("getHistoryByMonday", "onSuccess");
                            Log.i("historyMonday", String.valueOf(number));
                            if(number == 0){
                                percentHistoryMondayMutableLiveData.postValue(1.0F);
                            }else {
                                mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(trueNumber -> {
                                            Log.i("historyMondayTrueState", String.valueOf(trueNumber));
                                            Float percent = ((float)trueNumber / (float)number);
                                            Log.i("historyMondayPercent", String.valueOf(percent));
                                            percentHistoryMondayMutableLiveData.postValue(percent);
                                        });
                            }
                            callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                        }, throwable -> {
                            Log.e("getHistoryByMonday", "onError", throwable);
                            callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                        }
                )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryByTuesday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryByTuesday", "onSuccess");
                                    Log.i("historyTuesday", String.valueOf(number));
                                    if(number == 0){
                                        percentHistoryTuesdayMutableLiveData.postValue(1.0F);
                                    }else {
                                        mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(trueNumber -> {
                                                    Log.i("historyTuesdayTrueState", String.valueOf(trueNumber));
                                                    Float percent = ((float)trueNumber / (float)number);
                                                    Log.i("historyTuesdayPercent", String.valueOf(percent));
                                                    percentHistoryTuesdayMutableLiveData.postValue(percent);
                                                });
                                    }
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHistoryByTuesday", "onError", throwable);
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }
                        )
        );
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryByWednesday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryByWednesday", "onSuccess");
                                    Log.i("historyWednesday", String.valueOf(number));
                                    if(number == 0){
                                        percentHistoryWednesdayMutableLiveData.postValue(1.0F);
                                    }else {
                                        mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(trueNumber -> {
                                                    Log.i("historyWednesdayTrueState", String.valueOf(trueNumber));
                                                    Float percent = ((float)trueNumber / (float)number);
                                                    Log.i("historyWednesdayPercent", String.valueOf(percent));
                                                    percentHistoryWednesdayMutableLiveData.postValue(percent);
                                                });
                                    }
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHistoryByWednesday", "onError", throwable);
                                    callback.onCountTrueStateByHistoryDateFailure(mCompositeDisposable);
                                }
                        )
        );
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryByThursday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryByThursday", "onSuccess");
                                    Log.i("historyThursday", String.valueOf(number));
                                    if(number == 0){
                                        percentHistoryThursdayMutableLiveData.postValue(1.0F);
                                    }else {
                                        mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(trueNumber -> {
                                                    Log.i("historyThursdayTrueState", String.valueOf(trueNumber));
                                                    Float percent = ((float)trueNumber / (float)number);
                                                    Log.i("historyThursdayPercent", String.valueOf(percent));
                                                    percentHistoryThursdayMutableLiveData.postValue(percent);
                                                });
                                    }
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHistoryByThursday", "onError", throwable);
                                    callback.onCountTrueStateByHistoryDateFailure(mCompositeDisposable);
                                }
                        )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryByFriday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryByFriday", "onSuccess");
                                    Log.i("historyFriday", String.valueOf(number));
                                    if(number == 0){
                                        percentHistoryFridayMutableLiveData.postValue(1.0F);
                                    }else {
                                        mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(trueNumber -> {
                                                    Log.i("historyFridayTrueState", String.valueOf(trueNumber));
                                                    Float percent = ((float)trueNumber / (float)number);
                                                    Log.i("historyFridayPercent", String.valueOf(percent));
                                                    percentHistoryFridayMutableLiveData.postValue(percent);
                                                });
                                    }
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHistoryByFriday", "onError", throwable);
                                    callback.onCountTrueStateByHistoryDateFailure(mCompositeDisposable);
                                }
                        )
        );
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryBySaturday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryBySaturday", "onSuccess");
                                    Log.i("historySaturday", String.valueOf(number));
                                    if(number == 0){
                                        percentHistorySaturdayMutableLiveData.postValue(1.0F);
                                    }else {
                                        mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(trueNumber -> {
                                                    Log.i("historySaturdayTrueState", String.valueOf(trueNumber));
                                                    Float percent = ((float)trueNumber / (float)number);
                                                    Log.i("historySaturdayPercent", String.valueOf(percent));
                                                    percentHistorySaturdayMutableLiveData.postValue(percent);
                                                });
                                    }
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHistoryBySaturday", "onError", throwable);
                                    callback.onCountTrueStateByHistoryDateFailure(mCompositeDisposable);
                                }
                        )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void getHistoryBySunday(String historyDate, DbService.CountTrueStateByHistoryDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().countHistoriesByDate(historyDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(number -> {
                                    Log.i("getHistoryBySunday", "onSuccess");
                                    Log.i("historySunday", String.valueOf(number));
                                    if(number == 0){
                                        percentHistorySundayMutableLiveData.postValue(1.0F);
                                    }else {
                                        mHistoryRepository.getMHistoryDataSource().countTrueStateByHistoryDate(historyDate)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(trueNumber -> {
                                                    Log.i("historySundayTrueState", String.valueOf(trueNumber));
                                                    Float percent = ((float)trueNumber / (float)number);
                                                    Log.i("historySundayPercent", String.valueOf(percent));
                                                    percentHistorySundayMutableLiveData.postValue(percent);
                                                });
                                    }
                                    callback.onCountTrueStateByHistoryDate(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHistoryBySunday", "onError", throwable);
                                    callback.onCountTrueStateByHistoryDateFailure(mCompositeDisposable);
                                }
                        )
        );

    }

    @SuppressLint("LongLogTag")
    protected void getHabitListDescByLongestSteak(DbService.GetHabitByMostLongestSteak callback){
        mCompositeDisposable.add(mHabitRepository.getMHabitDataSource().getHabitByMostLongestSteak()
                .map(entity -> HabitMapper.getInstance().mapToModel(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(habitModel -> {
                    Log.i("getHabitByMostLongestSteak", "onComplete");
                    longestSteakHabitModelMutable.postValue(habitModel);
                    callback.onGetHabitByMostLongestSteakSuccess(mCompositeDisposable);
                }, throwable -> {
                    Log.i("getHabitByMostLongestSteak", "onError", throwable);
                    callback.onGetHabitByMostLongestSteakFailure(mCompositeDisposable);
                })
        );
    }

    protected void dispose(){
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }

}
