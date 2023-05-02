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
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
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

    private final MutableLiveData<Boolean> habitTodayLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> habitBeforeLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> habitAfterLD = new MutableLiveData<>();

    //Xoá đoạn này khi fix insert history sau 12h
    private final MutableLiveData<Boolean> insertHistoryLD = new MutableLiveData<>();
    public LiveData<Boolean> getInsertHistoryLD() {
        return insertHistoryLD;
    }

    public LiveData<Boolean> getHabitTodayLD() {
        return habitTodayLD;
    }

    public LiveData<Boolean> getHabitBeforeLD() {
        return habitBeforeLD;
    }

    public LiveData<Boolean> getHabitAfterLD() {
        return habitAfterLD;
    }



    private List<HabitModel> habitModelList = new ArrayList<>();
    private HabitAdapter mHabitAdapter = new HabitAdapter(habitModelList, habitInWeekModelList, recyclerViewClickListener);

    private List<HabitModel> habitModelBeforeList = new ArrayList<>();

    private List<HabitModel> habitModelAfterList = new ArrayList<>();

    private List<HabitModel> habitModelDoneList = new ArrayList<>();
    private DoneHabitAdapter mDoneHabitAdapter = new DoneHabitAdapter(habitModelDoneList);

    private List<HabitModel> habitModelFailedList = new ArrayList<>();
    private FailedHabitAdapter mFailedHabitAdapter = new FailedHabitAdapter(habitModelFailedList);

    private boolean hideToDo = false;
    private boolean hideDone = false;
    private boolean hideFailed = false;

    private final TimeUtils timeUtils = TimeUtils.getInstance();
    private final java.time.DayOfWeek day = timeUtils.getSelectedDate().getDayOfWeek();
    private final String dayName = day.getDisplayName(TextStyle.FULL, Locale.US);
    private Long dayOfWeekId;

    private DailyCalendarAdapter.OnClickItem onClickItem;

    protected DailyCalendarAdapter dailyCalendarAdapter;
    private List<LocalDate> days = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setDate(TextView text) {
        text.setText(timeUtils.getDateFromLocalDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setMonth(TextView text) {
        text.setText(timeUtils.getMonthYearFromDate());
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

    public DailyCalendarAdapter.OnClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(DailyCalendarAdapter.OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
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

    protected List<HabitModel> getHabitModelFailedList() {
        return habitModelFailedList;
    }

    protected List<HabitModel> getHabitModelList() {
        return habitModelList;
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

    private List<HistoryModel> historyModels = new ArrayList<>();

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
                        .io()), (habitEntities, habitInWeekEntities) -> {
                    List<Object> list = new ArrayList();
                    list.add(habitEntities);
                    list.add(habitInWeekEntities);
                    habitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                    habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                    return null;
                });
        addDisposable(result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CustomSubscriber<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        if (isSelectedTheDayAfter()) {
                            habitAfterLD.setValue(true);
                        }
                    }
                }));

    }

    /**
     * Gọi đồng thời 3 danh sách Habit, Habit In Weeks, History
     *
     * @param date
     */
    public void getHabitAndHistory(String date) {
        Long dateOfWeekId = timeUtils.getDayOfWeekId(date);
        Flowable<List<HabitEntity>> observable1 = getHabitsByUserId();
        Flowable<List<HabitInWeekEntity>> observable2 = getHabitInWeekModels(dateOfWeekId);
        Flowable<List<HistoryEntity>> observable3 = getHistoryByDate(date);

        Flowable<List<Object>> result =
                Flowable.zip(observable1.subscribeOn(Schedulers.io()), observable2.subscribeOn(Schedulers
                        .io()), observable3.subscribeOn(Schedulers.io()), (habitEntities, habitInWeekEntities, historyEntities) -> {
                    List<Object> list = new ArrayList();
                    list.add(habitEntities);
                    list.add(habitInWeekEntities);
                    list.add(historyEntities);
                    habitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                    habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                    historyModels = HistoryMapper.getInstance().mapToListModel(historyEntities);
                    //Xoá đoạn này khi fix được thêm History mới sau 12h
                    /*if (historyModels.size() == 0 && habitInWeekEntities.size() != 0) {
                        insertHistoriesList(date);
                    }*/
                    return list;
                });
        addDisposable(result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CustomSubscriber<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        if (isSelectedTheDayAfter()) {
                            habitAfterLD.postValue(true);
                        } else if (isSelectedTheDayBefore()) {
                            habitBeforeLD.postValue(true);
                        } else {
                            habitTodayLD.postValue(true);
                        }
                    }
                }));
    }

    /**
     * Đặt lại danh sách các list Habit (Habit, Habit done, Habit failed)
     */
    public void getHabitListByHistoryStatus() {
        mHabitAdapter.clear();
        mDoneHabitAdapter.clear();
        mFailedHabitAdapter.clear();
        for (HistoryModel history : historyModels) {
            if (history.getHistoryHabitsState().equals(VAL_NULL)) {
                habitModelList.add(getHabitById(habitsOfUser, history.getHabitId()));
            } else if (history.getHistoryHabitsState().equals(VAL_TRUE)) {
                habitModelDoneList.add(getHabitById(habitsOfUser, history.getHabitId()));
            } else {
                habitModelFailedList.add(getHabitById(habitsOfUser, history.getHabitId()));
            }
        }
    }

    public void clearHabitList() {
        habitModelList.clear();
        habitModelDoneList.clear();
        habitModelFailedList.clear();
    }

    /**
     * Thêm mới history cho ngày hôm nay
     *
     * @param historyTime
     */
    public void insertHistoriesList(String historyTime) {
        Log.d(TAG, "insertHistoriesList: ");
        if (historyModels.isEmpty() && habitInWeekModelList.isEmpty()) {
            return;
        }
        if (historyModels.size() == habitInWeekModelList.size()) {
            return;
        }
        if (historyModels.isEmpty() && !habitInWeekModelList.isEmpty()) {
            for (HabitInWeekModel habitInWeek : habitInWeekModelList) {
                HistoryModel model = new HistoryModel();
                model.setHistoryDate(historyTime);
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setHistoryHabitsState(VAL_NULL);
                model.setHabitId(habitInWeek.getHabitId());
                insertHistory(model);
                this.historyModels.add(model);
            }
        }
        insertHistoryLD.postValue(true);
    }

    public void insertHistory(HistoryModel historyModel) {
        mHistoryRepository.getMHistoryDataSource().insert(HistoryMapper.getInstance().mapToEntity(historyModel))
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i("insertHistory", "onComplete");
                    }
                });
    }

    public void getHabitListAfterDay(List<HabitInWeekModel> habitInWeekModels) {
        if (habitInWeekModels.size() == 0) {
            return;
        }
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

    public void updateHistory(int position, Class<?> adapterName, String value, String date) {
        HabitModel habitModel = new HabitModel();
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
        updateHistoryStatus(habitModel, date, value);
    }

    private void setHabitStatus(HabitModel habitModel, String status) {
        switch (status) {
            case VAL_NULL:
                habitModelList.add(habitModel);
                mHabitAdapter.notifyItemInserted(habitModelList.size() - 1);
                break;
            case VAL_TRUE:
                habitModelDoneList.add(habitModel);
                mDoneHabitAdapter.notifyItemInserted(habitModelDoneList.size() - 1);
                break;
            case VAL_FALSE:
                habitModelFailedList.add(habitModel);
                mFailedHabitAdapter.notifyItemInserted(habitModelFailedList.size() - 1);
                break;
            default:
                break;
        }
    }

    /**
     * Cập nhật trạng thái History
     *
     * @param habitModel
     * @param date
     * @param value
     */
    public void updateHistoryStatus(HabitModel habitModel, String date, String value) {
        mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(habitModel.getHabitId(), date)
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

    public boolean isSelectedToday() {
        if (calendarBarDate.equalsIgnoreCase(timeUtils.getDateTodayString())) {
            return true;
        }
        return false;
    }

    public boolean isSelectedTheDayBefore() {
        if (LocalDate.parse(calendarBarDate).isBefore(timeUtils.getSelectedDate())) {
            return true;
        }
        return false;
    }

    public boolean isSelectedTheDayAfter() {
        if (LocalDate.parse(calendarBarDate).isAfter(timeUtils.getSelectedDate())) {
            return true;
        }
        return false;
    }

}
