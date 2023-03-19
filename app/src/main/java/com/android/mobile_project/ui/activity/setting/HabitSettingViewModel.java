package com.android.mobile_project.ui.activity.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;
import com.android.mobile_project.data.remote.model.DayOfTimeModel;
import com.android.mobile_project.data.remote.model.DayOfWeekModel;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.repository.DayOfTimeRepository;
import com.android.mobile_project.data.repository.DayOfWeekRepository;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.repository.RemainderRepository;
import com.android.mobile_project.data.utils.mapper.DayOfTimeMapper;
import com.android.mobile_project.data.utils.mapper.DayOfWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.data.utils.mapper.RemainderMapper;
import com.android.mobile_project.ui.activity.setting.service.InitService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@MyCustomAnnotation.MyScope.ActivityScope
public class HabitSettingViewModel extends ViewModel implements IHabitSettingViewModel{

    private final HabitRepository mHabitRepository;
    private final RemainderRepository mRemainderRepository;
    private final HabitInWeekRepository mHabitInWeekRepository;
    private final DayOfWeekRepository mDayOfWeekRepository;
    private final DayOfTimeRepository mDayOfTimeRepository;
    private final HistoryRepository mHistoryRepository;

    @Inject
    public HabitSettingViewModel(HabitRepository mHabitRepository, RemainderRepository mRemainderRepository, HabitInWeekRepository mHabitInWeekRepository, DayOfWeekRepository mDayOfWeekRepository, DayOfTimeRepository mDayOfTimeRepository, HistoryRepository mHistoryRepository) {
        this.mHabitRepository = mHabitRepository;
        this.mRemainderRepository = mRemainderRepository;
        this.mHabitInWeekRepository = mHabitInWeekRepository;
        this.mDayOfWeekRepository = mDayOfWeekRepository;
        this.mDayOfTimeRepository = mDayOfTimeRepository;
        this.mHistoryRepository = mHistoryRepository;
    }

    protected InitService initService;

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

    @Override
    public HabitModel getHabitByUserIdAndHabitId(Long habitId){

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

    @Override
    public List<RemainderModel> getRemainderListByHabitId() {

        final List<RemainderModel>[] remainderModels = new List[]{new ArrayList<>()};

        mRemainderRepository.getMRemainderDataSource().getRemainderListByHabitId(habitModel.getHabitId())
                .observeOn(Schedulers.single())
                .subscribeWith(new DisposableSubscriber<List<RemainderEntity>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onNext(List<RemainderEntity> remainderEntities) {
                        Log.i("getRemainderListByHabitId", "onNext");
                        remainderModels[0] = RemainderMapper.getInstance().mapToListModel(remainderEntities);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable t) {
                        Log.e("getRemainderListByHabitId", "onError", t);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete() {
                        Log.i("getRemainderListByHabitId", "onComplete");
                    }
                });

        return remainderModels[0];
    }

    @Override
    public void updateRemainder(RemainderModel remainderModel) {
        mRemainderRepository.getMRemainderDataSource().update(RemainderMapper.getInstance().mapToEntity(remainderModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("updateRemainder", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("updateRemainder", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("updateRemainder", "onError", e);
                    }
                });
    }

    @Override
    public void deleteRemainder(RemainderModel remainderModel) {
        mRemainderRepository.getMRemainderDataSource().delete(RemainderMapper.getInstance().mapToEntity(remainderModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("deleteRemainder", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("deleteRemainder", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("deleteRemainder", "onError", e);
                    }
                });
    }

    @Override
    public HistoryModel getHistoryByHabitIdAndDate(Long habitId, String date) {
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

    protected List<HabitInWeekModel> getDayOfWeekHabitListByUserAndHabitId(){
        final List<HabitInWeekModel>[] habitInWeekModels = new List[]{new ArrayList<>()};

        mHabitInWeekRepository.getMHabitInWeekDataSource().getDayOfWeekHabitListByUserAndHabitId(
                DataLocalManager.getInstance().getUserId(), habitModel.getHabitId())
                .observeOn(Schedulers.single())
                .subscribeWith(new DisposableSubscriber<List<HabitInWeekEntity>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onNext(List<HabitInWeekEntity> habitInWeekEntities) {
                        Log.i("getDayOfWeekHabitListByUserAndHabitId", "onNext");
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
                        Log.i("getDayOfWeekHabitListByUserAndHabitId", "onComplete");
                    }
                });

        return habitInWeekModels[0];
    }

    protected DayOfWeekModel getDayOfWeekById(Long dayOfWeekId){
        final DayOfWeekModel[] dayOfWeekModels = new DayOfWeekModel[]{new DayOfWeekModel()};

        mDayOfWeekRepository.getMDayOfWeekDataSource().getDayOfWeekById(dayOfWeekId)
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<DayOfWeekEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("getDayOfWeekById","onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull DayOfWeekEntity dayOfWeekEntity) {
                        Log.i("getDayOfWeekById","onSuccess");
                        dayOfWeekModels[0] = DayOfWeekMapper.getInstance().mapToModel(dayOfWeekEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getDayOfWeekById","onError", e);
                    }
                });

        return dayOfWeekModels[0];
    }

    protected DayOfTimeModel getDayOfTimeById(){
        final DayOfTimeModel[] dayOfTimeModels = new DayOfTimeModel[]{new DayOfTimeModel()};

        mDayOfTimeRepository.getMDayOfTimeDataSource().getDayOfTimeById(habitModel.getHabitId())
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<DayOfTimeEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("getDayOfTimeById","onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull DayOfTimeEntity dayOfTimeEntity) {
                        Log.i("getDayOfTimeById","onSuccess");
                        dayOfTimeModels[0] = DayOfTimeMapper.getInstance().mapToModel(dayOfTimeEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getDayOfTimeById","onError", e);
                    }
                });

        return dayOfTimeModels[0];
    }

    protected RemainderModel checkExistRemainder(Long h, Long m){
        final RemainderModel[] remainderModels = new RemainderModel[]{new RemainderModel()};

        mRemainderRepository.getMRemainderDataSource().checkExistRemainder(h, m, habitModel.getHabitId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(new SingleObserver<RemainderEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("checkExistRemainder", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull RemainderEntity remainderEntity) {
                        Log.i("checkExistRemainder", "onSuccess");
                        remainderModels[0] = RemainderMapper.getInstance().mapToModel(remainderEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("checkExistRemainder", "onError", e);
                    }
                });

        return remainderModels[0];
    }

    protected void insertRemainder(RemainderModel remainderModel) {
        mRemainderRepository.getMRemainderDataSource().insert(RemainderMapper.getInstance().mapToEntity(remainderModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("insertRemainder", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertRemainder", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertRemainder", "onError", e);
                    }
                });
    }

    protected void deleteHabit() {
        mHabitRepository.getMHabitDataSource().delete(HabitMapper.getInstance().mapToEntity(habitModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("deleteHabit", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("deleteHabit", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("deleteHabit", "onError", e);
                    }
                });
    }

    protected void deleteHabitInWeek(HabitInWeekModel habitInWeekModel) {
        mHabitInWeekRepository.getMHabitInWeekDataSource().delete(HabitInWeekMapper.getInstance().mapToEntity(habitInWeekModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("deleteHabitInWeek", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("deleteHabitInWeek", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("deleteHabitInWeek", "onError", e);
                    }
                });
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

    protected void updateDateOfTimeInHabit(Long dayOfTimeId) {
        mHabitRepository.getMHabitDataSource().updateDateOfTimeInHabit(dayOfTimeId, habitModel.getHabitId())
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("updateDateOfTimeInHabit", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("updateDateOfTimeInHabit", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("updateDateOfTimeInHabit", "onError", e);
                    }
                });
    }

    protected void updateNameOfHabit(String habitName) {
        mHabitRepository.getMHabitDataSource().updateNameOfHabit(habitName, habitModel.getHabitId())
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("updateNameOfHabit", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("updateNameOfHabit", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("updateNameOfHabit", "onError", e);
                    }
                });
    }

}
