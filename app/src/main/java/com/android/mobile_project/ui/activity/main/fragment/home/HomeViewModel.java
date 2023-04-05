package com.android.mobile_project.ui.activity.main.fragment.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.repository.DayOfWeekRepository;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.ui.activity.base.BaseViewModel;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.AfterAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.BeforeAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.DbService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.UpdateService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@MyCustomAnnotation.MyScope.FragmentScope
@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeViewModel extends BaseViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();
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

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String VAL_TRUE = "true";

    private static final String VAL_FALSE = "false";

    private static final String VAL_NULL = "null";

    protected InitUIService initUIService;
    protected UpdateService updateService;
    protected HabitAdapter.RecyclerViewClickListener recyclerViewClickListener;
    protected InitUIService.InitHabitListUI initHabitListUI;


    private List<HabitInWeekModel> habitInWeekModelList = new ArrayList<>();
    private MutableLiveData<List<HabitInWeekModel>> habitInWeekModelListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HabitInWeekModel>> habitAfterMutableLiveData = new MutableLiveData<>();

    public LiveData<List<HabitInWeekModel>> getHabitInWeekModelListLD() {
        return habitInWeekModelListMutableLiveData;
    }

    public LiveData<List<HabitInWeekModel>> getHabitAfterLD() {
        return habitAfterMutableLiveData;
    }

    private MutableLiveData<List<HistoryModel>> historyModelListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HistoryModel>> historyInsertMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<HabitModel>> habitModelListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelList = new ArrayList<>();
    private HabitAdapter mHabitAdapter;

    private MutableLiveData<List<HabitModel>> habitModelBeforeListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HistoryModel>> historyBeforeListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelBeforeList = new ArrayList<>();
    private BeforeAdapter beforeAdapter;

    private MutableLiveData<List<HabitModel>> habitModelAfterListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelAfterList = new ArrayList<>();
    private AfterAdapter afterAdapter;

    private MutableLiveData<List<HabitModel>> habitModelDoneListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelDoneList = new ArrayList<>();
    private DoneHabitAdapter doneHabitAdapter;

    private MutableLiveData<List<HabitModel>> habitModelFailedListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelFailedList = new ArrayList<>();
    private FailedHabitAdapter failedHabitAdapter;

    private boolean hideToDo = false;
    private boolean hideDone = false;
    private boolean hideFailed = false;

    private final TimeUtils timeUtils = new TimeUtils();
    private final java.time.DayOfWeek day = timeUtils.getSelectedDate().getDayOfWeek();
    private final String dayName = day.getDisplayName(TextStyle.FULL, Locale.US);
    private Long dayOfWeekId;

    private DailyCalendarAdapter.OnClickItem onClickItem;

    protected DailyCalendarAdapter dailyCalendarAdapter;
    private List<LocalDate> days = new ArrayList<>();

    protected LiveData<List<HistoryModel>> getHistoryModelListLiveData() {
        return historyModelListMutableLiveData;
    }

    protected LiveData<List<HistoryModel>> getHistoryInsertLiveData() {
        return historyInsertMutableLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setDate(TextView text) {
        text.setText(timeUtils.getDateFromLocalDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setMonth(TextView text) {
        text.setText(timeUtils.getMonthYearFromDate());
    }

    protected LiveData<List<HabitModel>> getHabitModelBeforeListMutableLiveData() {
        return habitModelBeforeListMutableLiveData;
    }

    protected LiveData<List<HistoryModel>> getHistoryBeforeLD() {
        return historyBeforeListMutableLiveData;
    }

    public LiveData<List<HabitModel>> getHabitModelAfterListMutableLiveData() {
        return habitModelAfterListMutableLiveData;
    }

    protected List<HabitModel> getHabitModelBeforeList() {
        return habitModelBeforeList;
    }

    protected void setHabitModelBeforeList(List<HabitModel> habitModelBeforeList) {
        this.habitModelBeforeList = habitModelBeforeList;
    }

    protected List<HabitModel> getHabitModelAfterList() {
        return habitModelAfterList;
    }

    protected void setHabitModelAfterList(List<HabitModel> habitModelAfterList) {
        this.habitModelAfterList = habitModelAfterList;
    }

    protected BeforeAdapter getBeforeAdapter() {
        return beforeAdapter;
    }

    protected void setBeforeAdapter(BeforeAdapter beforeAdapter) {
        this.beforeAdapter = beforeAdapter;
    }

    protected AfterAdapter getAfterAdapter() {
        return afterAdapter;
    }

    protected void setAfterAdapter(AfterAdapter afterAdapter) {
        this.afterAdapter = afterAdapter;
    }

    protected LiveData<List<HabitModel>> getHabitModelListLiveData() {
        return habitModelListMutableLiveData;
    }

    protected LiveData<List<HabitModel>> getHabitModelDoneListLiveData() {
        return habitModelDoneListMutableLiveData;
    }

    protected LiveData<List<HabitModel>> getHabitModelFailedListLiveData() {
        return habitModelFailedListMutableLiveData;
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

    protected Long getDayOfWeekId() {
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

    protected HabitAdapter getmHabitAdapter() {
        return mHabitAdapter;
    }

    protected void setmHabitAdapter(HabitAdapter mHabitAdapter) {
        this.mHabitAdapter = mHabitAdapter;
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

    protected void getCurrentDayOfWeek() {
        Log.d(TAG, "getCurrentDayOfWeek: " + dayName);
        this.dayOfWeekId = timeUtils.getCurrentDayOfWeekId(this.dayName);
    }

    private List<HabitModel> habitsOfUser = new ArrayList<>();

    private MutableLiveData<List<HabitModel>> habitsOfUserMutableLiveData = new MutableLiveData<>();

    public LiveData<List<HabitModel>> getHabitsOfUserMutableLiveData() {
        return habitsOfUserMutableLiveData;
    }

    public void getHabitsByUserId() {
        mHabitRepository.getMHabitDataSource()
                .getHabitListByUserId(DataLocalManager.getInstance().getUserId())
                .subscribe(new CustomSubscriber<List<HabitEntity>>() {
                    @Override
                    public void onNext(List<HabitEntity> habitEntities) {
                        habitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                        habitsOfUserMutableLiveData.setValue(habitsOfUser);
                    }
                });
    }

    public void getHabitInWeekModels1(Long dateOfWeekId) {
        mHabitInWeekRepository.getMHabitInWeekDataSource().getHabitInWeekEntityByDayOfWeekId(DataLocalManager.getInstance().getUserId(), dateOfWeekId)
                .subscribe(new CustomSubscriber<List<HabitInWeekEntity>>() {
                    @Override
                    public void onNext(List<HabitInWeekEntity> habitInWeekEntities) {
                        Log.d(TAG, "onNext: habitInWeekEntities size = " + habitInWeekEntities.size());
                        habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                        habitInWeekModelListMutableLiveData.postValue(habitInWeekModelList);
                    }
                });
    }

    public void getHabitInWeekModels2(Long dateOfWeekId) {
        mHabitInWeekRepository.getMHabitInWeekDataSource().getHabitInWeekEntityByDayOfWeekId(DataLocalManager.getInstance().getUserId(), dateOfWeekId)
                .subscribe(new CustomSubscriber<List<HabitInWeekEntity>>() {
                    @Override
                    public void onNext(List<HabitInWeekEntity> habitInWeekEntities) {
                        Log.d(TAG, "onNext: habitInWeekEntities size = " + habitInWeekEntities.size());
                        habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                        habitAfterMutableLiveData.postValue(habitInWeekModelList);
                    }
                });
    }

    @SuppressLint("LongLogTag")
    protected void getHabitByUserIdAndHabitId1(Long habitId) {

        mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                .subscribe(new CustomSingleObserver<HabitEntity>() {
                    @Override
                    public void onSuccess(@NonNull HabitEntity habitEntity) {
                        habitModelAfterList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                        Log.d(TAG, "habitModelAfterList.size(): " + habitModelAfterList.size());
                        afterAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
                    }
                });
    }

    public void insertHistory(HistoryModel historyModel) {
        mHistoryRepository.getMHistoryDataSource().insert(HistoryMapper.getInstance().mapToEntity(historyModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i("insertHistory", "onComplete");
                    }
                });
    }

    public void getHabitsWhenClickDailyCalendar1(String date) {
        if (LocalDate.parse(date).equals(timeUtils.getSelectedDate())) {
            Log.d(TAG, "getHabitsWhenClickDailyCalendar1: if");
            habitModelBeforeList.clear();
            //beforeAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
            habitModelBeforeListMutableLiveData.postValue(habitModelBeforeList);
            habitModelAfterList.clear();
            //afterAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
            habitModelAfterListMutableLiveData.postValue(habitModelAfterList);
        } else {
            Log.d(TAG, "getHabitsWhenClickDailyCalendar1: else");
            if (LocalDate.parse(date).isBefore(timeUtils.getSelectedDate())) {
                mHistoryRepository.getMHistoryDataSource().getHistoryByDate(DataLocalManager.getInstance().getUserId(), date)
                        .subscribe(new CustomSubscriber<List<HistoryEntity>>() {
                            @Override
                            public void onNext(List<HistoryEntity> historyEntities) {
                                getHabitListByHistoryStatus(HistoryMapper.getInstance().mapToListModel(historyEntities), habitsOfUser);
                                historyBeforeListMutableLiveData.postValue(HistoryMapper.getInstance().mapToListModel(historyEntities));
                            }
                        });
            } else {
                Long id = timeUtils.getDayOfWeekId(date);
                Log.d(TAG, "getHabitsWhenClickDailyCalendar1: isAfter " + id + "---" + date);
                getHabitInWeekModels2(id);
            }
        }
    }

    public void getHabitListByHistoryStatus(List<HistoryModel> historyModels, List<HabitModel> habitModels) {
        habitModelList.clear();
        habitModelDoneList.clear();
        habitModelFailedList.clear();
        Log.d(TAG, "getHabitListByHistoryStatus: " + historyModels.size() + " - " + habitModels.size());
        for (HistoryModel history : historyModels) {
            if (history.getHistoryHabitsState().equals(VAL_NULL)) {
                Log.d(TAG, "habitModelList: ");
                habitModelList.add(getHabitById(habitModels, history.getHabitId()));
            } else if (history.getHistoryHabitsState().equals(VAL_TRUE)) {
                Log.d(TAG, "habitModelDoneList: ");
                habitModelDoneList.add(getHabitById(habitModels, history.getHabitId()));
            } else {
                Log.d(TAG, "habitModelFailedList: ");
                habitModelFailedList.add(getHabitById(habitModels, history.getHabitId()));
            }
        }
    }

    public void getHabitListAfterDay(List<HabitInWeekModel> habitInWeekModels) {
        habitModelList.clear();
        habitModelDoneList.clear();
        habitModelFailedList.clear();
        Log.d(TAG, "getHabitListAfterDay: " + habitInWeekModels.size() + " - " + habitsOfUser.size());
        for (HabitInWeekModel habitInWeek : habitInWeekModels) {
            habitModelList.add(getHabitById(habitsOfUser, habitInWeek.getHabitId()));
        }
    }

    private HabitModel getHabitById(List<HabitModel> habitModels, Long id) {
        for (HabitModel habit : habitModels) {
            if (habit.getHabitId() == id) {
                return habit;
            }
        }
        return null;
    }

    private boolean checkIsInsertHistory(List<HistoryModel> models, Long id) {
        for (HistoryModel history : models) {
            if (history.getHabitId() == id) {
                return true;
            }
        }
        return false;
    }


    public void insertHistoriesList(String historyTime, List<HistoryModel> models, List<HabitInWeekModel> habitInWeeks) {
        Log.d(TAG, "insertHistoriesList: " + models.size() + " - " + habitInWeeks.size());
        List<HistoryModel> historyModelList = new ArrayList<>();
        if (models.size() == 0) {
            for (HabitInWeekModel habitInWeek : habitInWeeks) {
                HistoryModel model = new HistoryModel();
                model.setHistoryDate(historyTime);
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setHistoryHabitsState(VAL_NULL);
                model.setHabitId(habitInWeek.getHabitId());
                insertHistory(model);
                historyModelList.add(model);
            }
        } else {
            historyModelList.addAll(models);
            for (HabitInWeekModel habitInWeek : habitInWeeks) {
                if (!checkIsInsertHistory(historyModelList, habitInWeek.getHabitId())) {
                    HistoryModel model = new HistoryModel();
                    model.setHistoryDate(historyTime);
                    model.setUserId(DataLocalManager.getInstance().getUserId());
                    model.setHistoryHabitsState(VAL_NULL);
                    model.setHabitId(habitInWeek.getHabitId());
                    insertHistory(model);
                    historyModelList.add(model);
                }
            }
        }
        Log.d(TAG, "insertHistoriesList end: " + historyModelList.size());
        historyInsertMutableLiveData.postValue(historyModelList);
    }

    @SuppressLint("LongLogTag")
    protected void getHabitByUserIdAndHabitId(List<HistoryModel> models, boolean isCurrentDate) {

        for (HistoryModel model : models) {
            if (model.getHistoryHabitsState().equals(VAL_NULL)) {
                mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), model.getHabitId())
                        .subscribe(habitEntity -> {
                                    Log.i("getHabitByUserIdAndHabitId", "onSuccess");
                                    if (isCurrentDate) {
                                        habitModelList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                                    }
                                    habitModelListMutableLiveData.postValue(habitModelList);
                                    //callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                                    //callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                                }
                        );
            } else if (model.getHistoryHabitsState().equals(VAL_TRUE)) {
                mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), model.getHabitId())
                        .subscribe(habitEntity -> {
                                    Log.i("getHabitByUserIdAndHabitId", "onSuccess");
                                    if (isCurrentDate) {
                                        habitModelDoneList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                                    }
                                    habitModelDoneListMutableLiveData.postValue(habitModelDoneList);
                                    //callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                                    //callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                                }
                        );
            } else {
                mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), model.getHabitId())
                        .subscribe(habitEntity -> {
                                    Log.i("getHabitByUserIdAndHabitId", "onSuccess");
                                    if (isCurrentDate) {
                                        habitModelFailedList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                                    }
                                    habitModelFailedListMutableLiveData.postValue(habitModelFailedList);
                                    //callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                                    //callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                                }
                        );
            }

        }


    }

    protected void getHistoryByDate(String historyTime) {
        mHistoryRepository.getMHistoryDataSource().getHistoryByDate(DataLocalManager.getInstance().getUserId(), historyTime)
                .subscribe(historyEntityList -> {
                    Log.i("getHistoryByDate", "onNext");
                    historyModelListMutableLiveData.postValue(HistoryMapper.getInstance().mapToListModel(historyEntityList));
                }, throwable -> {
                    Log.e("getHistoryByDate", "onError", throwable);
                });

    }

    protected void initCurrentAdapter(List<HistoryModel> models) {
        //getHabitByUserIdAndHabitId(models);
    }

    protected void updateHistory(int position, Class<?> adapterName, String
            value) {

        final HabitModel habitModel;

        if (HabitAdapter.class.equals(adapterName)) {
            habitModel = habitModelList.get(position);
            habitModelList.remove(position);
            mHabitAdapter.notifyItemRemoved(position);
            habitModelListMutableLiveData.postValue(habitModelList);
        } else if (DoneHabitAdapter.class.equals(adapterName)) {
            habitModel = habitModelDoneList.get(position);
            habitModelDoneList.remove(position);
            doneHabitAdapter.notifyItemRemoved(position);
            habitModelDoneListMutableLiveData.postValue(habitModelDoneList);
        } else {
            habitModel = habitModelFailedList.get(position);
            habitModelFailedList.remove(position);
            failedHabitAdapter.notifyItemRemoved(position);
            habitModelFailedListMutableLiveData.postValue(habitModelFailedList);
        }

        getHistoryByHabitIdAndDate(habitModel, LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT)), value);


    }

    public void getHistoryByHabitIdAndDate(HabitModel habitModel, String date, String value) {
        mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(habitModel.getHabitId(), date)
                .subscribe(historyEntity -> {
                            Log.i(TAG, "getHistoryByHabitIdAndDate onSuccess");
                            HistoryModel model = HistoryMapper.getInstance().mapToModel(historyEntity);
                            model.setHistoryHabitsState(value);
                            mHistoryRepository.getMHistoryDataSource().update(HistoryMapper.getInstance().mapToEntity(model))
                                    .subscribe(() -> {
                                                Log.i(TAG, "updateHistory onComplete");
                                                //callback.onUpdateHistorySuccess(mCompositeDisposable);
                                            }, throwable -> {
                                                Log.e(TAG, "updateHistory onError", throwable);
                                                //callback.onUpdateHistoryFailure(mCompositeDisposable);
                                            }
                                    );

                            switch (value) {
                                case VAL_NULL:
                                    habitModelList.add(habitModel);
                                    mHabitAdapter.notifyItemInserted(habitModelList.size() - 1);
                                    habitModelListMutableLiveData.postValue(habitModelList);
                                    break;
                                case VAL_TRUE:
                                    habitModelDoneList.add(habitModel);
                                    doneHabitAdapter.notifyItemInserted(habitModelDoneList.size() - 1);
                                    habitModelDoneListMutableLiveData.postValue(habitModelDoneList);
                                    break;
                                case VAL_FALSE:
                                    habitModelFailedList.add(habitModel);
                                    failedHabitAdapter.notifyItemInserted(habitModelFailedList.size() - 1);
                                    habitModelFailedListMutableLiveData.postValue(habitModelFailedList);
                                    break;
                                default:
                                    break;
                            }
                        }, throwable -> {
                            Log.e(TAG, "getHistoryByHabitIdAndDate onError", throwable);
                            //callback.onGetHistoryByHabitIdAndDateFailure();
                        }
                );
    }

    public void getListHistoryByListHabit() {

    }

}
