package com.android.mobile_project.ui.activity.create;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.ui.activity.create.service.DbService;
import com.android.mobile_project.ui.activity.create.service.InitService;
import com.android.mobile_project.ui.activity.create.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.DayOfTime;
import com.android.mobile_project.utils.time.DayOfWeek;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.SneakyThrows;

@MyCustomAnnotation.MyScope.ActivityScope
public class CreateHabitViewModel extends ViewModel {

    private final HabitRepository mHabitRepository;
    private final HabitInWeekRepository mHabitInWeekRepository;

    @Inject
    public CreateHabitViewModel(HabitRepository mHabitRepository, HabitInWeekRepository mHabitInWeekRepository) {
        this.mHabitRepository = mHabitRepository;
        this.mHabitInWeekRepository = mHabitInWeekRepository;
    }

    InitService initService;

    ToastService toastService;

    private final Long userId = DataLocalManager.getInstance().getUserId();

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

    private final MutableLiveData<HabitModel> habitModelMutableLiveData = new MutableLiveData<>();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void disposeCompositeDisposable() {
        mCompositeDisposable.dispose();
    }

    protected LiveData<HabitModel> getHabitModelMutableLiveData(){
        return habitModelMutableLiveData;
    }

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

    private final DbService.InsertHabitInWeek insertHabitInWeekCallBack = new DbService.InsertHabitInWeek() {
        @SuppressLint("LongLogTag")
        @Override
        public void onInsertHabitInWeekSuccess() {
            Log.i("insertHabitInWeekSuccess", "isSuccess");
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onInsertHabitInWeekFailure() {
            Log.e("insertHabitInWeekSuccess", "isFailure");
        }
    };

    @SneakyThrows
    protected void insertHabit(final String habitName, DbService.InsertHabit callback) {

        if(!isDayOfWeekSelected()){
            toastService.makeDaysOfWeekInputtedIsEmptyToast();
            return;
        }

        final HabitModel model = new HabitModel();
        model.setHabitName(habitName);
        model.setHabitLogo(null);
        model.setNumOfLongestSteak(0L);
        model.setUserId(userId);

        if(selectAnytime){
            model.setDayOfTimeId(DayOfTime.ANYTIME.getId());
        }else if(selectMorning){
            model.setDayOfTimeId(DayOfTime.MORNING.getId());
        }else if(selectNight){
            model.setDayOfTimeId(DayOfTime.NIGHT.getId());
        }else if(selectAfternoon) {
            model.setDayOfTimeId(DayOfTime.AFTERNOON.getId());
        }else {
            toastService.makeDayOfTimeInputtedIsEmptyToast();
            return;
        }

        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().insert(HabitMapper.getInstance().mapToEntity(model))
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                                    Log.i("insertHabit", "onComplete");
                                    callback.onInsertHabitSuccess();
                                    checkExistHabitByName(habitName, new DbService.GetHabitByName() {
                                        @Override
                                        public void onGetHabitByNameSuccess(Long id) {
                                            Log.i("CreateHabitViewModel", "onGetHabitByNameSuccess");
                                            if (selectSunDate) {
                                                insertHabitInWeek(id, DayOfWeek.SUN.getId(), insertHabitInWeekCallBack);
                                            }

                                            if (selectMonDate) {
                                                insertHabitInWeek(id, DayOfWeek.MON.getId(), insertHabitInWeekCallBack);
                                            }

                                            if (selectTueDate) {
                                                insertHabitInWeek(id, DayOfWeek.TUE.getId(), insertHabitInWeekCallBack);
                                            }

                                            if (selectWedDate) {
                                                insertHabitInWeek(id, DayOfWeek.WED.getId(), insertHabitInWeekCallBack);
                                            }

                                            if (selectThuDate) {
                                                insertHabitInWeek(id, DayOfWeek.THU.getId(), insertHabitInWeekCallBack);
                                            }

                                            if (selectFriDate) {
                                                insertHabitInWeek(id, DayOfWeek.FRI.getId(), insertHabitInWeekCallBack);
                                            }

                                            if (selectSatDate) {
                                                insertHabitInWeek(id, DayOfWeek.SAT.getId(), insertHabitInWeekCallBack);
                                            }

                                        }

                                        @Override
                                        public void onGetHabitByNameFailure() {
                                            Log.e("CreateHabitViewModel", "onGetHabitByNameFailure");
                                        }
                                    });
                                }, throwable -> {
                                    Log.e("insertHabit", "onError", throwable);
                                    callback.onInsertHabitFailure();
                                }
                        )
        );

    }

    protected void checkExistHabitByName(String habitName, DbService.GetHabitByName callback){

        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().getHabitByName(habitName)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(habitEntity -> {
                    Log.i("getHabitByName", "onSuccess");
                    callback.onGetHabitByNameSuccess(habitEntity.getHabitId());
                }, throwable -> {
                    Log.e("getHabitByName", "onError", throwable);
                    callback.onGetHabitByNameFailure();
                })
        );

    }

    protected void insertHabitInWeek(Long habitId, Long dayOfWeekId, DbService.InsertHabitInWeek callback) {

        mCompositeDisposable.add(
                mHabitInWeekRepository.getMHabitInWeekDataSource()
                        .insert(HabitInWeekMapper.getInstance().mapToEntity(new HabitInWeekModel(userId, habitId, dayOfWeekId, 0L, 0L, 0L)))
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("insertHabitInWeek", "onComplete");
                            callback.onInsertHabitInWeekSuccess();
                        }, throwable -> {
                            Log.e("insertHabitInWeek", "onError", throwable);
                            callback.onInsertHabitInWeekFailure();
                        })
        );

    }

    private boolean isDayOfWeekSelected(){
        return isSelectSunDate() || isSelectMonDate() || isSelectTueDate() || isSelectWedDate()
                || isSelectThuDate() || isSelectFriDate() || isSelectSatDate();
    }

}
