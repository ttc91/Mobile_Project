package com.android.mobile_project.ui.activity.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.repository.DayOfTimeRepository;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.repository.RemainderRepository;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.data.utils.mapper.RemainderMapper;
import com.android.mobile_project.ui.activity.setting.adapter.RemainderAdapter;
import com.android.mobile_project.ui.activity.setting.service.DbService;
import com.android.mobile_project.ui.activity.setting.service.InitService;
import com.android.mobile_project.ui.activity.setting.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.notification.NotificationWorker;
import com.android.mobile_project.utils.time.DayOfTime;
import com.android.mobile_project.utils.time.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@MyCustomAnnotation.MyScope.ActivityScope
public class HabitSettingViewModel extends ViewModel implements IHabitSettingViewModel{

    private final HabitRepository mHabitRepository;
    private final RemainderRepository mRemainderRepository;
    private final HabitInWeekRepository mHabitInWeekRepository;
    private final HistoryRepository mHistoryRepository;
    private Context context;

    @Inject
    public HabitSettingViewModel(HabitRepository mHabitRepository, RemainderRepository mRemainderRepository, HabitInWeekRepository mHabitInWeekRepository, HistoryRepository mHistoryRepository) {
        this.mHabitRepository = mHabitRepository;
        this.mRemainderRepository = mRemainderRepository;
        this.mHabitInWeekRepository = mHabitInWeekRepository;
        this.mHistoryRepository = mHistoryRepository;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected InitService initService;
    protected ToastService toastService;

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

    private HabitModel habitModel = new HabitModel();
    private List<HabitInWeekModel> habitInWeekModelList = new ArrayList<>();
    private List<RemainderModel> remainderModelList = new ArrayList<>();

    private String habitCurrentName = "";

    private RemainderAdapter mRemainderAdapter;

    /**
     * Mutable Live Data properties:
     */

    private final MutableLiveData<HabitModel> mHabitModelMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<HabitInWeekModel>> mHabitInWeekListMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Long> mDayOfTimeModelIdMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isUpdateHabitNameMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isDeleteHabitInWeekMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isUpdateDayOfTimeMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isInsertOrRemoveRemainderModelListMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Integer> isUpdateRemainderItemMutableLiveData = new MutableLiveData<>();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public RemainderAdapter getMRemainderAdapter() {
        return mRemainderAdapter;
    }

    public void setMRemainderAdapter(RemainderAdapter mRemainderAdapter) {
        this.mRemainderAdapter = mRemainderAdapter;
    }

    protected LiveData<Integer> getUpdateRemainderMutableLiveData(){
        return isUpdateRemainderItemMutableLiveData;
    }

    protected LiveData<Boolean> getInsertOrRemoveRemainderModelListMutableLiveData(){
        return isInsertOrRemoveRemainderModelListMutableLiveData;
    }

    protected LiveData<Boolean> getUpdateDayOfTimeMutableLiveData(){
        return isUpdateDayOfTimeMutableLiveData;
    }

    protected LiveData<Boolean> getUpdateHabitNameResultMutableLiveData(){
        return isUpdateHabitNameMutableLiveData;
    }

    protected LiveData<Boolean> getDeleteHabitInWeekResultMutableLiveData(){
        return isDeleteHabitInWeekMutableLiveData;
    }

    protected LiveData<HabitModel> getMHabitModelMutableLiveData(){
        return mHabitModelMutableLiveData;
    }

    protected LiveData<List<HabitInWeekModel>> getMHabitInWeekListMutableLiveData(){
        return mHabitInWeekListMutableLiveData;
    }

    protected LiveData<Long> getMDayOfTimeModelIdMutableLiveData(){
        return mDayOfTimeModelIdMutableLiveData;
    }

    public HabitModel getHabitModel() {
        return habitModel;
    }

    public void setHabitModel(HabitModel habitModel) {
        this.habitModel = habitModel;
    }

    public List<RemainderModel> getRemainderModelList() {
        return remainderModelList;
    }

    public void setRemainderModelList(List<RemainderModel> remainderModelList) {
        this.remainderModelList = remainderModelList;
    }

    public List<HabitInWeekModel> getHabitInWeekModelList() {
        return habitInWeekModelList;
    }

    public void setHabitInWeekModelList(List<HabitInWeekModel> habitInWeekModelList) {
        this.habitInWeekModelList = habitInWeekModelList;
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

    @SuppressLint("LongLogTag")
    @Override
    public void getHabitByUserIdAndHabitId(Long habitId, DbService.GetHabitByUserIdAndHabitIdResult callback){
        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                    .map(habitEntity -> HabitMapper.getInstance().mapToModel(habitEntity))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(habitModel -> {
                        Log.i("getHabitByUserIdAndHabitId","onSuccess");
                        mHabitModelMutableLiveData.postValue(habitModel);
                        habitCurrentName = habitModel.getHabitName();

                        if(habitModel.getDayOfTimeId().equals(DayOfTime.ANYTIME.getId())){
                            setSelectAnytime(true);
                            mDayOfTimeModelIdMutableLiveData.postValue(DayOfTime.ANYTIME.getId());
                        }else if(habitModel.getDayOfTimeId().equals(DayOfTime.AFTERNOON.getId())){
                            setSelectAfternoon(true);
                            mDayOfTimeModelIdMutableLiveData.postValue(DayOfTime.AFTERNOON.getId());
                        }else if(habitModel.getDayOfTimeId().equals(DayOfTime.MORNING.getId())){
                            setSelectMorning(true);
                            mDayOfTimeModelIdMutableLiveData.postValue(DayOfTime.MORNING.getId());
                        }else if(habitModel.getDayOfTimeId().equals(DayOfTime.NIGHT.getId())){
                            setSelectNight(true);
                            mDayOfTimeModelIdMutableLiveData.postValue(DayOfTime.NIGHT.getId());
                        }

                        callback.onGetHabitByUserIdAndHabitIdSuccess(habitModel, mCompositeDisposable);
                    }, throwable -> {
                        Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                        callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                    })
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    @Override
    public void getRemainderListByHabitId(DbService.GetRemainderListByHabitIdResult callback) {
        mCompositeDisposable.add(
                mRemainderRepository.getMRemainderDataSource().getRemainderListByHabitId(mHabitModelMutableLiveData.getValue().getHabitId())
                    .map(remainderEntities -> RemainderMapper.getInstance().mapToListModel(remainderEntities))
                    .observeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .subscribe(remainderModels -> {
                        Log.i("getRemainderListByHabitId", "onNext");
                        remainderModelList = remainderModels;
                        callback.onGetRemainderListByHabitIdSuccess(mCompositeDisposable);
                    },throwable -> {
                        Log.e("getRemainderListByHabitId", "onError", throwable);
                        callback.onGetRemainderListByHabitIdFailurẹ(mCompositeDisposable);
                    })
        );
    }

    @Override
    public void updateRemainder(int position, Long hourOld, Long minutesOld, Long hourNew, Long minutesNew, DbService.UpdateRemainderResult callback) {
        checkExistRemainder(hourOld, minutesOld, new DbService.CheckExistRemainderResult() {
            @SuppressLint("LongLogTag")
            @Override
            public void onCheckExistRemainderSuccess(Long remainderId, CompositeDisposable disposable) {
                Log.i("HabitSettingViewModel-onCheckExistRemainderSuccess(UseToUpdate)", "onCheckExistRemainderSuccess");
                RemainderModel model = new RemainderModel(remainderId, mHabitModelMutableLiveData.getValue().getHabitId(), hourNew, minutesNew);
                mCompositeDisposable.add(
                        mRemainderRepository.getMRemainderDataSource().update(RemainderMapper.getInstance().mapToEntity(model))
                                .observeOn(Schedulers.single())
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                            Log.i("updateRemainder", "onComplete");
                                            remainderModelList.get(position).setHourTime(hourNew);
                                            remainderModelList.get(position).setMinutesTime(minutesNew);
                                            isUpdateRemainderItemMutableLiveData.postValue(position);
                                            callback.onUpdateRemainderSuccess(mCompositeDisposable);
                                        }, throwable -> {
                                            Log.e("updateRemainder", "onError", throwable);
                                            callback.onUpdateRemainderFailurẹ(mCompositeDisposable);
                                        }
                                )
                );
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onCheckExistRemainderFailure(CompositeDisposable disposable) {
                Log.i("HabitSettingViewModel-onCheckExistRemainderSuccess(UseToUpdate)", "onCheckExistRemainderFailure");
                disposable.clear();
            }
        });

    }

    @Override
    public void deleteRemainderByTimerHourAndTimerMinutesAndId(Long h, Long m, DbService.DeleteRemainderResult callback) {
        mCompositeDisposable.add(
                mRemainderRepository.getMRemainderDataSource().deleteRemainderByTimerHourAndTimerMinutesAndId(
                        h, m, mHabitModelMutableLiveData.getValue().getHabitId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        Log.i("deleteRemainder", "onComplete");
                        callback.onDeleteRemainderSuccess(mCompositeDisposable);
                        isInsertOrRemoveRemainderModelListMutableLiveData.postValue(false);
                    }, throwable -> {
                        Log.e("deleteRemainder", "onError", throwable);
                        callback.onDeleteRemainderFailure(mCompositeDisposable);
                    }
                )
        );

    }

    @SuppressLint("LongLogTag")
    @Override
    public void getHistoryByHabitIdAndDate(Long habitId, String date, DbService.GetHistoryByHabitAndDateResult callback) {

        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(habitId, date)
                        .map(entity -> HistoryMapper.getInstance().mapToModel(entity))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(historyModel -> {
                            Log.i("getHistoryByHabitIdAndDate", "onSuccess");
                            callback.onGetHistoryByHabitAndDateSuccess(historyModel, mCompositeDisposable);
                        }, throwable -> {
                            Log.e("getHistoryByHabitIdAndDate", "onError", throwable);
                            callback.onGetHistoryByHabitAndDateFailure(mCompositeDisposable);
                        }
                )
        );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    protected void getDayOfWeekHabitListByUserAndHabitId(Long habitId, DbService.GetHabitInWeekHabitListByUserAndHabitId callback){
        mCompositeDisposable.add(
                mHabitInWeekRepository.getMHabitInWeekDataSource().getDayOfWeekHabitListByUserAndHabitId(
                        DataLocalManager.getInstance().getUserId(), habitId)
                        .map(habitInWeekEntities -> HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(habitInWeekModels -> {
                            Log.i("getDayOfWeekHabitListByUserAndHabitId", "onNext");
                            mHabitInWeekListMutableLiveData.postValue(habitInWeekModels);
                            habitInWeekModelList = habitInWeekModels;
                            for(HabitInWeekModel model : habitInWeekModels){
                                if(model.getDayOfWeekId().equals(DayOfWeek.SUN.getId())){
                                    setSelectSunDate(true);
                                }else if(model.getDayOfWeekId().equals(DayOfWeek.MON.getId())){
                                    setSelectMonDate(true);
                                }else if(model.getDayOfWeekId().equals(DayOfWeek.TUE.getId())){
                                    setSelectTueDate(true);
                                }else if(model.getDayOfWeekId().equals(DayOfWeek.WED.getId())){
                                    setSelectWedDate(true);
                                }else if(model.getDayOfWeekId().equals(DayOfWeek.THU.getId())){
                                    setSelectThuDate(true);
                                }else if(model.getDayOfWeekId().equals(DayOfWeek.FRI.getId())){
                                    setSelectFriDate(true);
                                }else if(model.getDayOfWeekId().equals(DayOfWeek.SAT.getId())){
                                    setSelectSatDate(true);
                                }
                            }
                            callback.onGetHabitInWeekHabitListByUserAndHabitIdSuccess(mCompositeDisposable);
                        }, throwable -> {
                            Log.e("getDayOfWeekHabitListByUserAndHabitId", "onError", throwable);
                            callback.onGetHabitInWeekHabitListByUserAndHabitIdFailure(mCompositeDisposable);
                        }
                )
        );

    }

    private void checkExistRemainder(Long h, Long m, DbService.CheckExistRemainderResult callback){
        mCompositeDisposable.add(
                mRemainderRepository.getMRemainderDataSource().checkExistRemainder(h, m, mHabitModelMutableLiveData.getValue().getHabitId())
                        .map(remainderEntity -> RemainderMapper.getInstance().mapToModel(remainderEntity))
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.single())
                        .subscribe(remainderModel -> {
                                    Log.i("checkExistRemainder", "onSuccess");
                                    callback.onCheckExistRemainderSuccess(remainderModel.getRemainderId(), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("checkExistRemainder", "onError", throwable);
                                    callback.onCheckExistRemainderFailure(mCompositeDisposable);
                                }
                        )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void checkExistAndInsertRemainder(Long h, Long m, DbService.CheckExistRemainderResult callback){
        mCompositeDisposable.add(
                mRemainderRepository.getMRemainderDataSource().checkExistRemainder(h, m, mHabitModelMutableLiveData.getValue().getHabitId())
                        .map(remainderEntity -> RemainderMapper.getInstance().mapToModel(remainderEntity))
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.single())
                        .subscribe(remainderModel -> {
                            Log.i("checkExistRemainder", "onSuccess");
                            callback.onCheckExistRemainderSuccess(remainderModel.getRemainderId(), mCompositeDisposable);
                        }, throwable -> {
                            Log.e("checkExistRemainder", "onError", throwable);
                            callback.onCheckExistRemainderFailure(mCompositeDisposable);
                            RemainderModel model = new RemainderModel(null, mHabitModelMutableLiveData.getValue().getHabitId(), h, m);
                            insertRemainder(model, new DbService.InsertRemainderResult() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onInsertRemainderSuccess(CompositeDisposable disposable) {
                                    Log.i("HabitSettingViewModel-insertRemainder", "onInsertRemainderSuccess");
                                    disposable.clear();
                                    NotificationWorker.enqueueWorkerWithHabit(context, mHabitModelMutableLiveData.getValue(), model, mHabitInWeekListMutableLiveData.getValue());
                                }

                                @SuppressLint("LongLogTag")
                                @Override
                                public void onInsertRemainderFailure(CompositeDisposable disposable) {
                                    Log.i("HabitSettingViewModel-insertRemainder", "onInsertRemainderFailure");
                                    disposable.clear();
                                }
                            });
                        }
                )
        );
    }

    protected void insertRemainder(RemainderModel remainderModel, DbService.InsertRemainderResult callback) {
        mCompositeDisposable.add(
                mRemainderRepository.getMRemainderDataSource().insert(RemainderMapper.getInstance().mapToEntity(remainderModel))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("insertRemainder", "onComplete");
                            callback.onInsertRemainderSuccess(mCompositeDisposable);
                            //After insert update to RemainderList post value is true - means insert
                            remainderModelList.add(remainderModel);
                            isInsertOrRemoveRemainderModelListMutableLiveData.postValue(true);
                        }, throwable -> {
                            Log.e("insertRemainder", "onError", throwable);
                            callback.onInsertRemainderFailure(mCompositeDisposable);
                        }
                )
        );
    }

    protected void deleteHabit(DbService.DeleteHabitResult callback) {
        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().delete(HabitMapper.getInstance().mapToEntity(habitModel))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("deleteHabit", "onComplete");
                            callback.onDeleteHabitSuccess(mCompositeDisposable);
                        }, throwable -> {
                            Log.e("deleteHabit", "onError", throwable);
                            callback.onDeleteHabitFailure(mCompositeDisposable);
                        }
                )
        );
    }

    @SuppressLint("LongLogTag")
    protected void deleteHabitInWeekByHabitId(DbService.DeleteHabitInWeekResult callback) {

        mCompositeDisposable.add(
                mHabitInWeekRepository.getMHabitInWeekDataSource().deleteHabitInWeekByHabitId(mHabitModelMutableLiveData.getValue().getHabitId())
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                () -> {
                                    Log.i("deleteHabitInWeekByHabitId", "onComplete");
                                    isDeleteHabitInWeekMutableLiveData.postValue(true);
                                    callback.onDeleteHabitInWeekSuccess(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("deleteHabitInWeekByHabitId", "onError", throwable);
                                    callback.onDeleteHabitInWeekFailure(mCompositeDisposable);
                                }
                        )
        );

    }

    protected void insertHabitInWeek(Long dayOfWeekId, Long timerHour, Long timerMinutes, Long timerSecond, DbService.InsertHabitInWeekResult callback) {

        HabitInWeekModel model = new HabitInWeekModel(DataLocalManager.getInstance().getUserId(), mHabitModelMutableLiveData.getValue().getHabitId(),
                dayOfWeekId, timerHour, timerMinutes, timerSecond);

        mCompositeDisposable.add(
                mHabitInWeekRepository.getMHabitInWeekDataSource().insert(HabitInWeekMapper.getInstance().mapToEntity(model))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        Log.i("insertHabitInWeek", "onComplete");
                        callback.onInsertHabitInWeekSuccess(mCompositeDisposable);
                    }, throwable -> {
                        Log.e("insertHabitInWeek", "onError", throwable);
                        callback.onInsertHabitInWeekFailure(mCompositeDisposable);
                    }
                )
        );


    }

    protected void updateDateOfTimeInHabit(DbService.UpdateDateOfTimeInHabitResult callback) {

        Long dayOfTimeId = 0L;

        if(selectAnytime){
            dayOfTimeId = DayOfTime.ANYTIME.getId();
        }else if(selectNight){
            dayOfTimeId = DayOfTime.NIGHT.getId();
        }else if(selectAfternoon){
            dayOfTimeId = DayOfTime.AFTERNOON.getId();
        }else if(selectMorning){
            dayOfTimeId = DayOfTime.MORNING.getId();
        }

        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().updateDateOfTimeInHabit(dayOfTimeId, mHabitModelMutableLiveData.getValue().getHabitId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                () -> {
                                    Log.i("updateDateOfTimeInHabit", "onComplete");
                                    isUpdateDayOfTimeMutableLiveData.postValue(true);
                                    callback.onUpdateDateOfTimeInHabitSuccess(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("updateDateOfTimeInHabit", "onError", throwable);
                                    callback.onUpdateDateOfTimeInHabitFailure(mCompositeDisposable);
                                }
                        )
        );

    }

    protected void checkExistHabitByName(String habitName, DbService.GetHabitByNameResult callback){

        if(habitName.equals(habitCurrentName)){
            callback.onGetHabitByNameFailure(mCompositeDisposable);
        }else {
            mCompositeDisposable.add(
                    mHabitRepository.getMHabitDataSource().getHabitByName(habitName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(habitEntity -> {
                                Log.i("getHabitByName", "onSuccess");
                                callback.onGetHabitByNameSuccess(mCompositeDisposable, habitEntity.getHabitId());
                            }, throwable -> {
                                Log.e("getHabitByName", "onError", throwable);
                                callback.onGetHabitByNameFailure(mCompositeDisposable);
                            }
                    )
            );
        }

    }

    protected void updateNameOfHabit(String habitName, DbService.UpdateNameOfHabitResult callback) {
        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().updateNameOfHabit(habitName, mHabitModelMutableLiveData.getValue().getHabitId())
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("updateNameOfHabit", "onComplete");
                            isUpdateHabitNameMutableLiveData.postValue(true);
                            callback.onUpdateNameOfHabitSuccess(mCompositeDisposable);

                        }, throwable -> {
                            Log.e("updateNameOfHabit", "onError", throwable);
                            callback.onUpdateNameOfHabitFailure(mCompositeDisposable);
                        })
        );

    }

    protected void dispose(){
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }

}
