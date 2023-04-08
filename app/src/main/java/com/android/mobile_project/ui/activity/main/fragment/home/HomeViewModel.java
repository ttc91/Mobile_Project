package com.android.mobile_project.ui.activity.main.fragment.home;

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
import com.android.mobile_project.utils.custom.SingleLiveEvent;
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
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function3;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    protected HabitAdapter.RecyclerViewClickListener recyclerViewClickListener;

    private List<HabitInWeekModel> habitInWeekModelList = new ArrayList<>();

    public List<HabitInWeekModel> getHabitInWeekModelList() {
        return habitInWeekModelList;
    }

    public void setHabitInWeekModelList(List<HabitInWeekModel> habitInWeekModelList) {
        this.habitInWeekModelList = habitInWeekModelList;
    }

    public String getCalendarBarDate() {
        return calendarBarDate;
    }

    public void setCalendarBarDate(String calendarBarDate) {
        this.calendarBarDate = calendarBarDate;
    }

    private String calendarBarDate;


    private SingleLiveEvent<List<HabitInWeekModel>> habitInWeekModelListMutableLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<List<HabitInWeekModel>> habitAfterMutableLiveData = new SingleLiveEvent<>();

    public LiveData<List<HabitInWeekModel>> getHabitInWeekModelListLD() {
        return habitInWeekModelListMutableLiveData;
    }

    public LiveData<List<HabitInWeekModel>> getHabitAfterLD() {
        return habitAfterMutableLiveData;
    }

    private SingleLiveEvent<List<HistoryModel>> historyModelListMutableLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<List<HistoryModel>> historyInsertMutableLiveData = new SingleLiveEvent<>();

    private List<HabitModel> habitModelList = new ArrayList<>();
    private HabitAdapter mHabitAdapter = new HabitAdapter(habitModelList, recyclerViewClickListener);

    private MutableLiveData<List<HabitModel>> habitModelBeforeListMutableLiveData = new MutableLiveData<>();
    private SingleLiveEvent<List<HistoryModel>> historyBeforeListMutableLiveData = new SingleLiveEvent<>();
    private List<HabitModel> habitModelBeforeList = new ArrayList<>();
    private BeforeAdapter beforeAdapter;

    private MutableLiveData<List<HabitModel>> habitModelAfterListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelAfterList = new ArrayList<>();
    private AfterAdapter afterAdapter;

    private List<HabitModel> habitModelDoneList = new ArrayList<>();
    private DoneHabitAdapter mDoneHabitAdapter = new DoneHabitAdapter(habitModelDoneList);

    private List<HabitModel> habitModelFailedList = new ArrayList<>();
    private FailedHabitAdapter mFailedHabitAdapter = new FailedHabitAdapter(habitModelFailedList);

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

    protected DoneHabitAdapter getmDoneHabitAdapter() {
        return mDoneHabitAdapter;
    }

    protected void setmDoneHabitAdapter(DoneHabitAdapter mDoneHabitAdapter) {
        this.mDoneHabitAdapter = mDoneHabitAdapter;
    }

    protected FailedHabitAdapter getmFailedHabitAdapter() {
        return mFailedHabitAdapter;
    }

    protected void setmFailedHabitAdapter(FailedHabitAdapter mFailedHabitAdapter) {
        this.mFailedHabitAdapter = mFailedHabitAdapter;
    }

    protected void getCurrentDayOfWeek() {
        Log.d(TAG, "getCurrentDayOfWeek: " + dayName);
        this.dayOfWeekId = timeUtils.getCurrentDayOfWeekId(this.dayName);
    }

    private List<HabitModel> habitsOfUser = new ArrayList<>();

    public List<HabitModel> getHabitsOfUser() {
        return habitsOfUser;
    }

    public void setHabitsOfUser(List<HabitModel> habitsOfUser) {
        this.habitsOfUser = habitsOfUser;
    }

    private List<HistoryModel> historyModels = new ArrayList<>();

    public List<HistoryModel> getHistoryModels() {
        return historyModels;
    }

    public void setHistoryModels(List<HistoryModel> historyModels) {
        this.historyModels = historyModels;
    }

    public Flowable<List<HabitEntity>> getHabitsByUserId() {
        return mHabitRepository.getMHabitDataSource()
                .getHabitListByUserId(DataLocalManager.getInstance().getUserId());
    }

    public Flowable<List<HabitInWeekEntity>> getHabitInWeekModels(Long dateOfWeekId) {
        return mHabitInWeekRepository.getMHabitInWeekDataSource()
                .getHabitInWeekEntityByDayOfWeekId(DataLocalManager.getInstance().getUserId(), dateOfWeekId);
    }

    public Flowable<List<HistoryEntity>> getHistoryByDate(String historyTime) {
        return mHistoryRepository.getMHistoryDataSource()
                .getHistoryByDate(DataLocalManager.getInstance().getUserId(), historyTime);
    }

    public void getHabitAndHabitInWeek(Long dateOfWeekId) {
        Flowable<List<HabitEntity>> observable1 = getHabitsByUserId();
        Flowable<List<HabitInWeekEntity>> observable2 = getHabitInWeekModels(dateOfWeekId);

        Flowable<List<Object>> result =
                Flowable.zip(observable1.subscribeOn(Schedulers.io()), observable2.subscribeOn(Schedulers
                        .io()), new BiFunction<List<HabitEntity>, List<HabitInWeekEntity>, List<Object>>() {
                    @Override
                    public List<Object> apply(List<HabitEntity> habitEntities, List<HabitInWeekEntity> habitInWeekEntities) throws Throwable {
                        List<Object> list = new ArrayList();
                        list.add(habitEntities);
                        list.add(habitInWeekEntities);
                        Log.d(TAG, "getHabitAndHistory: " + "habitEntities: " + habitEntities.size()
                                + " habitInWeekEntities: " + habitInWeekEntities.size());

                        habitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                        habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                        return null;
                    }
                });
        addDisposable(result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CustomSubscriber<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        mLiveDataIsSuccess.postValue(true);
                    }
                }));

    }

    public void getHabitAndHistory(Long dateOfWeekId, String historyTime) {
        Flowable<List<HabitEntity>> observable1 = getHabitsByUserId();
        Flowable<List<HabitInWeekEntity>> observable2 = getHabitInWeekModels(dateOfWeekId);
        Flowable<List<HistoryEntity>> observable3 = getHistoryByDate(historyTime);

        Flowable<List<Object>> result =
                Flowable.zip(observable1.subscribeOn(Schedulers.io()), observable2.subscribeOn(Schedulers
                        .io()), observable3.subscribeOn(Schedulers.io()), new Function3<List<HabitEntity>, List<HabitInWeekEntity>, List<HistoryEntity>, List<Object>>() {
                    @Override
                    public List<Object> apply(List<HabitEntity> habitEntities, List<HabitInWeekEntity> habitInWeekEntities, List<HistoryEntity> historyEntities) throws Throwable {
                        List<Object> list = new ArrayList();
                        list.add(habitEntities);
                        list.add(habitInWeekEntities);
                        list.add(historyEntities);
                        Log.d(TAG, "getHabitAndHistory: " + "habitEntities: " + habitEntities.size()
                                + "habitInWeekEntities: " + habitInWeekEntities.size() + "historyEntities: " + historyEntities.size());
                        habitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                        habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                        historyModels = HistoryMapper.getInstance().mapToListModel(historyEntities);
                        return list;
                    }
                });
        addDisposable(result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CustomSubscriber<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        mLiveDataIsSuccess.postValue(true);
                    }
                }));
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

    public void getHabitsWhenClickDailyCalendar(String date) {
        if (LocalDate.parse(date).equals(timeUtils.getSelectedDate())) {
            Log.d(TAG, "getHabitsWhenClickDailyCalendar: if");
            habitModelBeforeList.clear();
            //beforeAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
            habitModelBeforeListMutableLiveData.postValue(habitModelBeforeList);
            habitModelAfterList.clear();
            //afterAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
            habitModelAfterListMutableLiveData.postValue(habitModelAfterList);
        } else {
            Log.d(TAG, "getHabitsWhenClickDailyCalendar: else");
            if (LocalDate.parse(date).isBefore(timeUtils.getSelectedDate())) {
                mHistoryRepository.getMHistoryDataSource().getHistoryByDateSingle(DataLocalManager.getInstance().getUserId(), date)
                        .subscribe(new CustomSingleObserver<List<HistoryEntity>>() {
                            @Override
                            public void onSuccess(@NonNull List<HistoryEntity> historyEntities) {
                                Log.d(TAG, "getHistoryByDate: " + historyEntities.size());
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
        mHabitAdapter.clear();
        mDoneHabitAdapter.clear();
        mFailedHabitAdapter.clear();
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
            if (habit.getHabitId().equals(id)) {
                return habit;
            }
        }
        return null;
    }

    private boolean checkIsInsertHistory(List<HistoryModel> models, Long id) {
        for (HistoryModel history : models) {
            if (history.getHabitId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public void insertHistoriesList(String historyTime, List<HistoryModel> histories, List<HabitInWeekModel> habitInWeeks) {
        Log.d(TAG, "insertHistoriesList: " + histories.size() + " - " + habitInWeeks.size());
        if (histories.isEmpty() && habitInWeeks.isEmpty()) {
            return;
        }
        List<HistoryModel> historyModelList = new ArrayList<>();
        if (histories.isEmpty() && !habitInWeeks.isEmpty()) {
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
            for (HabitInWeekModel habitInWeek : habitInWeeks) {
                if (!checkIsInsertHistory(histories, habitInWeek.getHabitId())) {
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

    protected void updateHistory(int position, Class<?> adapterName, String
            value) {

        HabitModel habitModel = new HabitModel();
//        Log.d(TAG, "Before updateHistory: " + habitModelList.size()
//                + " -- " + habitModelDoneList.size() + " -- " + habitModelFailedList.size());
        if (HabitAdapter.class.equals(adapterName)) {
            if (habitModelList.size() != 0) {
                habitModel = habitModelList.get(position);
                habitModelList.remove(position);
                setHabitStatus(habitModel, value);
                mHabitAdapter.notifyItemRemoved(position);
            }
        } else if (DoneHabitAdapter.class.equals(adapterName)) {
            if (habitModelDoneList.size() != 0) {
                habitModel = habitModelDoneList.get(position);
                habitModelDoneList.remove(position);
                setHabitStatus(habitModel, value);
                mDoneHabitAdapter.notifyItemRemoved(position);
            }
        } else {
            if (habitModelFailedList.size() != 0) {
                habitModel = habitModelFailedList.get(position);
                habitModelFailedList.remove(position);
                setHabitStatus(habitModel, value);
                mFailedHabitAdapter.notifyItemRemoved(position);
            }
        }

        Log.d(TAG, "After updateHistory: " + mHabitAdapter.getItemCount()
                + " -- " + mDoneHabitAdapter.getItemCount() + " -- " + mFailedHabitAdapter.getItemCount());
        updateHistoryStatus(habitModel, LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT)), value);
    }

    private void setHabitStatus(HabitModel habitModel, String status) {
        switch (status) {
            case VAL_NULL:
                habitModelList.add(habitModel);
                mHabitAdapter.notifyItemInserted(habitModelList.size() - 1);
                break;
            case VAL_TRUE:
                habitModelDoneList.add(habitModel);
                mDoneHabitAdapter.notifyItemInserted(habitModelList.size() - 1);
                break;
            case VAL_FALSE:
                habitModelFailedList.add(habitModel);
                mFailedHabitAdapter.notifyItemInserted(habitModelList.size() - 1);
                break;
            default:
                break;
        }
    }

    public void updateHistoryStatus(HabitModel habitModel, String date, String value) {
        mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(habitModel.getHabitId(), date)
                .subscribe(new CustomSingleObserver<HistoryEntity>() {
                    @Override
                    public void onSuccess(@NonNull HistoryEntity historyEntity) {
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
                    }
                });
    }

}
