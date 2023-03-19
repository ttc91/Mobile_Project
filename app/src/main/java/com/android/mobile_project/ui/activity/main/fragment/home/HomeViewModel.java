package com.android.mobile_project.ui.activity.main.fragment.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.remote.model.DayOfWeekModel;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.repository.DayOfWeekRepository;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.utils.mapper.DayOfWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.UpdateService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@MyCustomAnnotation.MyScope.FragmentScope
@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeViewModel extends ViewModel implements IHomeViewModel{

    private final DayOfWeekRepository mDayOfWeekRepository;
    private final HabitInWeekRepository mHabitInWeekRepository;
    private final HistoryRepository mHistoryRepository;
    private final HabitRepository mHabitRepository;

    @Inject
    public HomeViewModel(DayOfWeekRepository mDayOfWeekRepository, HabitInWeekRepository mHabitInWeekRepository,
                         HistoryRepository mHistoryRepository, HabitRepository mHabitRepository) {
        this.mDayOfWeekRepository = mDayOfWeekRepository;
        this.mHabitInWeekRepository = mHabitInWeekRepository;
        this.mHistoryRepository = mHistoryRepository;
        this.mHabitRepository = mHabitRepository;
    }

    protected InitUIService initUIService;
    protected UpdateService updateService;
    protected HabitAdapter.RecyclerViewClickListener recyclerViewClickListener;

    private List<HabitModel> habitModelList = new ArrayList<>();
    private HabitAdapter adapter;

    private List<HabitModel> habitModelDoneList = new ArrayList<>();
    private DoneHabitAdapter doneHabitAdapter;

    private List<HabitModel> habitModelFailedList = new ArrayList<>();
    private FailedHabitAdapter failedHabitAdapter;

    private List<HistoryModel> historyModelList = new ArrayList<>();

    private boolean hideToDo = false;
    private boolean hideDone = false;
    private boolean hideFailed = false;

    private final TimeUtils timeUtils = new TimeUtils();
    private final java.time.DayOfWeek day = timeUtils.getSelectedDate().getDayOfWeek();
    private final String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());
    private Long dayOfWeekId;

    private DailyCalendarAdapter.OnClickItem onClickItem;

    protected DailyCalendarAdapter dailyCalendarAdapter;
    private List<LocalDate> days = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setDate(TextView text){

        TimeUtils utils = new TimeUtils();
        text.setText(utils.getDateFromLocalDate());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setMonth(TextView text){
        TimeUtils utils = new TimeUtils();
        text.setText(utils.getMonthYearFromDate());
    }

    public DailyCalendarAdapter.OnClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(DailyCalendarAdapter.OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public DailyCalendarAdapter getDailyCalendarAdapter() {
        return dailyCalendarAdapter;
    }

    public void setDailyCalendarAdapter(DailyCalendarAdapter dailyCalendarAdapter) {
        this.dailyCalendarAdapter = dailyCalendarAdapter;
    }

    public List<LocalDate> getDays() {
        return days;
    }

    public void setDays(List<LocalDate> days) {
        this.days = days;
    }

    protected Long getDayOfWeekId(){
        return this.dayOfWeekId;
    }

    protected boolean isHideToDo() {
        return hideToDo;
    }

    protected void setHideToDo(boolean hideToDo) {
        this.hideToDo = hideToDo;
    }

    protected boolean isHideDone() {
        return hideDone;
    }

    protected void setHideDone(boolean hideDone) {
        this.hideDone = hideDone;
    }

    protected boolean isHideFailed() {
        return hideFailed;
    }

    protected void setHideFailed(boolean hideFailed) {
        this.hideFailed = hideFailed;
    }

    protected List<HistoryModel> getHistoryModelList() {
        return historyModelList;
    }

    protected void setHistoryModelList(List<HistoryModel> historyModelList) {
        this.historyModelList = historyModelList;
    }

    protected List<HabitModel> getHabitModelDoneList() {
        return habitModelDoneList;
    }

    protected void setHabitModelDoneList(List<HabitModel> habitModelDoneList) {
        this.habitModelDoneList = habitModelDoneList;
    }

    protected List<HabitModel> getHabitModelFailedList() {
        return habitModelFailedList;
    }

    protected void setHabitModelFailedList(List<HabitModel> habitModelFailedList) {
        this.habitModelFailedList = habitModelFailedList;
    }

    protected List<HabitModel> getHabitModelList() {
        return habitModelList;
    }

    protected void setHabitModelList(List<HabitModel> habitModelList) {
        this.habitModelList = habitModelList;
    }

    protected HabitAdapter getAdapter() {
        return adapter;
    }

    protected void setAdapter(HabitAdapter adapter) {
        this.adapter = adapter;
    }

    protected DoneHabitAdapter getDoneHabitAdapter() {
        return doneHabitAdapter;
    }

    protected void setDoneHabitAdapter(DoneHabitAdapter doneHabitAdapter) {
        this.doneHabitAdapter = doneHabitAdapter;
    }

    protected FailedHabitAdapter getFailedHabitAdapter() {
        return failedHabitAdapter;
    }

    protected void setFailedHabitAdapter(FailedHabitAdapter failedHabitAdapter) {
        this.failedHabitAdapter = failedHabitAdapter;
    }

    protected void getCurrentDayOfWeek(){

        final DayOfWeekModel[] model = {new DayOfWeekModel()};

        mDayOfWeekRepository.getMDayOfWeekDataSource().searchDayOfWeekByName(this.dayName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<DayOfWeekEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("searchDayOfWeekByName","onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull DayOfWeekEntity dayOfWeekEntity) {
                        Log.i("searchDayOfWeekByName", "onSuccess");
                        model[0] = DayOfWeekMapper.getInstance().mapToModel(dayOfWeekEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("searchDayOfWeekByName", "onError", e);
                    }
                });

        this.dayOfWeekId = model[0].getDayOfWeekId();

    }

    protected DayOfWeekModel getDayOfWeekModel(String dayName){

        final DayOfWeekModel[] model = {new DayOfWeekModel()};

        mDayOfWeekRepository.getMDayOfWeekDataSource().searchDayOfWeekByName(dayName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<DayOfWeekEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("searchDayOfWeekByName","onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull DayOfWeekEntity dayOfWeekEntity) {
                        Log.i("searchDayOfWeekByName", "onSuccess");
                        model[0] = DayOfWeekMapper.getInstance().mapToModel(dayOfWeekEntity);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("searchDayOfWeekByName", "onError", e);
                    }
                });

        this.dayOfWeekId = model[0].getDayOfWeekId();

        return model[0];

    }

    protected List<HabitInWeekModel> getHabitInWeekModels(){
        final List<HabitInWeekModel>[] habitInDayOfWeeks = new List[]{new ArrayList<>()};

        mHabitInWeekRepository.getMHabitInWeekDataSource().getHabitInWeekEntityByDayOfWeekId(this.dayOfWeekId)
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableSubscriber<List<HabitInWeekEntity>>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onNext(List<HabitInWeekEntity> habitInWeekEntities) {
                        Log.i("getHabitInWeekEntityByDayOfWeekId","onNext");
                        habitInDayOfWeeks[0] = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable t) {
                        Log.e("getHabitInWeekEntityByDayOfWeekId", "onError", t);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete() {
                        Log.i("getHabitInWeekEntityByDayOfWeekId","onComplete");
                    }
                });
        return habitInDayOfWeeks[0];
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

    protected List<HistoryModel> getHistoryByDate(String historyTime){

        final List<HistoryModel>[] historyModels = new List[]{new ArrayList<>()};

        mHistoryRepository.getMHistoryDataSource().getHistoryByDate(DataLocalManager.getInstance().getUserId(), historyTime)
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableSubscriber<List<HistoryEntity>>() {
                    @Override
                    public void onNext(List<HistoryEntity> historyEntityList) {
                        Log.i("getHistoryByDate","onNext");
                        historyModels[0] = HistoryMapper.getInstance().mapToListModel(historyEntityList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("getHistoryByDate", "onError", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("getHistoryByDate","onComplete");
                    }
                });
        return historyModels[0];
    }

    protected void insertHistory(HistoryModel historyModel){
        mHistoryRepository.getMHistoryDataSource()
                .insert(HistoryMapper.getInstance().mapToEntity(historyModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("insertHistory", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertHistory","onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertHistory", "onError", e);
                    }
                });
    }

    protected void updateHabit(HabitModel habitModel){
        mHabitRepository.getMHabitDataSource().update(HabitMapper.getInstance().mapToEntity(habitModel))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    Log.i("updateHabit", "onSubscribe");
                }

                @Override
                public void onComplete() {
                    Log.i("updateHabit", "onComplete");
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.e("updateHabit", "onError", e);
                }
            });
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

    @Override
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
    @Override
    public List<HabitInWeekModel> getDayOfWeekHabitListByUserAndHabitId(Long habitId){

        final List<HabitInWeekModel>[] habitInWeekModels = new List[]{new ArrayList<>()};

        mHabitInWeekRepository.getMHabitInWeekDataSource().getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                .observeOn(Schedulers.single())
                .subscribeWith(new DisposableSubscriber<List<HabitInWeekEntity>>() {
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

}
