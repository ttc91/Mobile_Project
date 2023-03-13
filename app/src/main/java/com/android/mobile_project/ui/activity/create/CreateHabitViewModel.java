package com.android.mobile_project.ui.activity.create;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.remote.model.DayOfTimeModel;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.repository.DayOfTimeRepository;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.utils.mapper.DayOfTimeMapper;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.ui.activity.create.service.InitService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@MyCustomAnnotation.MyScope.ActivityScope
public class CreateHabitViewModel extends ViewModel {

    private final DayOfTimeRepository mDayOfTimeRepository;
    private final HabitRepository mHabitRepository;
    private final HabitInWeekRepository mHabitInWeekRepository;

    @Inject
    public CreateHabitViewModel(DayOfTimeRepository mDayOfTimeRepository, HabitRepository mHabitRepository, HabitInWeekRepository mHabitInWeekRepository) {
        this.mDayOfTimeRepository = mDayOfTimeRepository;
        this.mHabitRepository = mHabitRepository;
        this.mHabitInWeekRepository = mHabitInWeekRepository;
    }

    InitService initService;

    private boolean selectSunDate = false;
    private boolean selectMonDate = false;
    private boolean selectTueDate = false;
    private boolean selectWedDate = false;
    private boolean selectThuDate = false;
    private boolean selectFriDate = false;
    private boolean selectSatDate = false;

    private boolean selectAnytime = false;
    private boolean selectNight = false;
    private boolean selectMorning = false;
    private boolean selectAfternoon = false;

    public boolean isSelectSunDate() {
        return selectSunDate;
    }

    public void setSelectSunDate(boolean selectSunDate) {
        this.selectSunDate = selectSunDate;
    }

    public boolean isSelectMonDate() {
        return selectMonDate;
    }

    public void setSelectMonDate(boolean selectMonDate) {
        this.selectMonDate = selectMonDate;
    }

    public boolean isSelectTueDate() {
        return selectTueDate;
    }

    public void setSelectTueDate(boolean selectTueDate) {
        this.selectTueDate = selectTueDate;
    }

    public boolean isSelectWedDate() {
        return selectWedDate;
    }

    public void setSelectWedDate(boolean selectWedDate) {
        this.selectWedDate = selectWedDate;
    }

    public boolean isSelectThuDate() {
        return selectThuDate;
    }

    public void setSelectThuDate(boolean selectThuDate) {
        this.selectThuDate = selectThuDate;
    }

    public boolean isSelectFriDate() {
        return selectFriDate;
    }

    public void setSelectFriDate(boolean selectFriDate) {
        this.selectFriDate = selectFriDate;
    }

    public boolean isSelectSatDate() {
        return selectSatDate;
    }

    public void setSelectSatDate(boolean selectSatDate) {
        this.selectSatDate = selectSatDate;
    }

    public boolean isSelectAnytime() {
        return selectAnytime;
    }

    public void setSelectAnytime(boolean selectAnytime) {
        this.selectAnytime = selectAnytime;
    }

    public boolean isSelectNight() {
        return selectNight;
    }

    public void setSelectNight(boolean selectNight) {
        this.selectNight = selectNight;
    }

    public boolean isSelectMorning() {
        return selectMorning;
    }

    public void setSelectMorning(boolean selectMorning) {
        this.selectMorning = selectMorning;
    }

    public boolean isSelectAfternoon() {
        return selectAfternoon;
    }

    public void setSelectAfternoon(boolean selectAfternoon) {
        this.selectAfternoon = selectAfternoon;
    }

    protected DayOfTimeModel searchDayOfTimeByName(String dayOfTime){

        final DayOfTimeModel[] dayOfTimeModels = new DayOfTimeModel[]{new DayOfTimeModel()};

        mDayOfTimeRepository.getMDayOfTimeDataSource().searchDayOfTimeByName(dayOfTime)
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<DayOfTimeEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("searchDayOfTimeByName", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull DayOfTimeEntity dayOfTimeEntity) {
                        Log.i("searchDayOfTimeByName", "onSuccess");
                        dayOfTimeModels[0] = DayOfTimeMapper.getInstance().mapToModel(dayOfTimeEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("searchDayOfTimeByName", "onError", e);
                    }
                });

        return dayOfTimeModels[0];

    }

    protected void insertHabit(HabitModel habitModel) {
        mHabitRepository.getMHabitDataSource().insert(HabitMapper.getInstance().mapToEntity(habitModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("insertHabit", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertHabit", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertHabit", "onError", e);
                    }
                });
    }

    protected HabitModel getHabitByName(String habitName){

        final HabitModel[] habitModels = new HabitModel[]{new HabitModel()};

        mHabitRepository.getMHabitDataSource().getHabitByName(habitName)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new SingleObserver<HabitEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("getHabitByName", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull HabitEntity habitEntity) {
                        Log.i("getHabitByName", "onSuccess");
                        habitModels[0] = HabitMapper.getInstance().mapToModel(habitEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getHabitByName", "onError", e);
                    }
                });
        return habitModels[0];
    }

    protected void insertHabitInWeek(HabitInWeekModel habitInWeekModel) {
        mHabitInWeekRepository.getMHabitInWeekDataSource().insert(HabitInWeekMapper.getInstance().mapToEntity(habitInWeekModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("insertHabitInWeek", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertHabitInWeek", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertHabitInWeek", "onError", e);
                    }
                });
    }

}
