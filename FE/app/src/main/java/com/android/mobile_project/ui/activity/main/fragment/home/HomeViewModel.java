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
import io.reactivex.rxjava3.functions.Consumer;
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

    private List<HabitInWeekModel> mHabitInWeekModelList = new ArrayList<>();

    public List<HabitInWeekModel> getmHabitInWeekModelList() {
        return mHabitInWeekModelList;
    }

    public void setmHabitInWeekModelList(List<HabitInWeekModel> mHabitInWeekModelList) {
        this.mHabitInWeekModelList = mHabitInWeekModelList;
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

    //Transition sang count down activity
    private final MutableLiveData<HabitInWeekModel> countDownTimerLD = new MutableLiveData<>();

    public LiveData<HabitInWeekModel> getCountDownTimerLD() {
        return countDownTimerLD;
    }

    public LiveData<Boolean> getHabitTodayLD() {
        return habitTodayLD;
    }

    public LiveData<Boolean> getHabitBeforeLD() {
        return habitBeforeLD;
    }

    public MutableLiveData<Boolean> getHabitAfterLD() {
        return habitAfterLD;
    }


    private List<HabitModel> mHabitModelList = new ArrayList<>();
    private HabitAdapter mHabitAdapter = new HabitAdapter(mHabitModelList, mHabitInWeekModelList, recyclerViewClickListener);

    private List<HabitModel> mHabitModelDoneList = new ArrayList<>();
    private DoneHabitAdapter mDoneHabitAdapter = new DoneHabitAdapter(mHabitModelDoneList);

    private List<HabitModel> mHabitModelFailedList = new ArrayList<>();
    private FailedHabitAdapter mFailedHabitAdapter = new FailedHabitAdapter(mHabitModelFailedList);

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

    protected List<HabitModel> getmHabitModelDoneList() {
        return mHabitModelDoneList;
    }

    protected List<HabitModel> getmHabitModelFailedList() {
        return mHabitModelFailedList;
    }

    protected List<HabitModel> getmHabitModelList() {
        return mHabitModelList;
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

    private List<HabitModel> mHabitsOfUser = new ArrayList<>();

    private List<HistoryModel> mHistoryModels = new ArrayList<>();

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
                    mHabitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                    mHabitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
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
        Log.d(TAG, "getHabitAndHistory: " + date);
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
                    mHabitsOfUser = HabitMapper.getInstance().mapToListModel(habitEntities);
                    mHabitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                    mHistoryModels = HistoryMapper.getInstance().mapToListModel(historyEntities);
                    //Xoá đoạn này khi fix được thêm History mới sau 12h
                    /*if (historyModels.size() == 0 && habitInWeekEntities.size() != 0) {
                        insertHistoriesList(date);
                    }*/
                    return list;
                });

        addDisposable(result
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new CustomSubscriber<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        if (isSelectedTheDayAfter()) {
                            Log.d(TAG, "habitAfterLD: postValue");
                            habitAfterLD.postValue(true);
                        } else if (isSelectedTheDayBefore()) {
                            Log.d(TAG, "habitBeforeLD: postValue");
                            habitBeforeLD.postValue(true);
                        } else {
                            Log.d(TAG, "habitTodayLD: postValue");
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
        for (HistoryModel history : mHistoryModels) {
            if (history.getHistoryHabitsState().equals(VAL_NULL)) {
                mHabitModelList.add(getHabitById(mHabitsOfUser, history.getHabitId()));
            } else if (history.getHistoryHabitsState().equals(VAL_TRUE)) {
                mHabitModelDoneList.add(getHabitById(mHabitsOfUser, history.getHabitId()));
            } else {
                mHabitModelFailedList.add(getHabitById(mHabitsOfUser, history.getHabitId()));
            }
        }
    }

    public void clearHabitList() {
        mHabitModelList.clear();
        mHabitModelDoneList.clear();
        mHabitModelFailedList.clear();
        mHabitsOfUser.clear();
        mHistoryModels.clear();
        mHabitInWeekModelList.clear();
    }

    /**
     * Thêm mới history cho ngày hôm nay
     *
     * @param historyTime
     */
    public void insertHistoriesList(String historyTime) {
        Log.d(TAG, "insertHistoriesList: ");
        if (mHistoryModels.isEmpty() && mHabitInWeekModelList.isEmpty()) {
            return;
        }
        if (mHistoryModels.size() == mHabitInWeekModelList.size()) {
            return;
        }
        if (mHistoryModels.isEmpty() && !mHabitInWeekModelList.isEmpty()) {
            for (HabitInWeekModel habitInWeek : mHabitInWeekModelList) {
                HistoryModel model = new HistoryModel();
                model.setHistoryDate(historyTime);
                model.setUserId(DataLocalManager.getInstance().getUserId());
                model.setHistoryHabitsState(VAL_NULL);
                model.setHabitId(habitInWeek.getHabitId());
                insertHistory(model);
                this.mHistoryModels.add(model);
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
            mHabitModelList.add(getHabitById(mHabitsOfUser, habitInWeek.getHabitId()));
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

    private HabitInWeekModel getHabitInWeekById(List<HabitInWeekModel> habitModels, Long id) {
        for (HabitInWeekModel habit : habitModels) {
            if (habit.getHabitId().equals(id)) {
                return habit;
            }
        }
        return null;
    }

    public void updateHistory(int position, Class<?> adapterName, String value, String date, boolean isCountDown) {
        HabitModel habitModel = new HabitModel();
        HabitInWeekModel habitInWeekModel = new HabitInWeekModel();
        if (HabitAdapter.class.equals(adapterName)) {
            if (mHabitModelList.size() != 0) {
                habitModel = mHabitModelList.get(position);
                habitInWeekModel = getHabitInWeekById(mHabitInWeekModelList, habitModel.getHabitId());
                mHabitModelList.remove(position);
                setHabitStatus(habitModel, value);
                mHabitAdapter.notifyItemRemoved(position);
            }
        } else if (DoneHabitAdapter.class.equals(adapterName)) {
            if (mHabitModelDoneList.size() != 0) {
                habitModel = mHabitModelDoneList.get(position);
                habitInWeekModel = getHabitInWeekById(mHabitInWeekModelList, habitModel.getHabitId());
                mHabitModelDoneList.remove(position);
                setHabitStatus(habitModel, value);
                mDoneHabitAdapter.notifyItemRemoved(position);
            }
        } else {
            if (mHabitModelFailedList.size() != 0) {
                habitModel = mHabitModelFailedList.get(position);
                habitInWeekModel = getHabitInWeekById(mHabitInWeekModelList, habitModel.getHabitId());
                mHabitModelFailedList.remove(position);
                setHabitStatus(habitModel, value);
                mFailedHabitAdapter.notifyItemRemoved(position);
            }
        }
        if (habitInWeekModel.isTimerHabit() && isCountDown) {
            countDownTimerLD.postValue(habitInWeekModel);
        } else {
            updateHistoryStatus(habitModel, date, value);
        }
    }

    private void setHabitStatus(HabitModel habitModel, String status) {
        switch (status) {
            case VAL_NULL:
                mHabitModelList.add(habitModel);
                mHabitAdapter.notifyItemInserted(mHabitModelList.size() - 1);
                break;
            case VAL_TRUE:
                mHabitModelDoneList.add(habitModel);
                mDoneHabitAdapter.notifyItemInserted(mHabitModelDoneList.size() - 1);
                break;
            case VAL_FALSE:
                mHabitModelFailedList.add(habitModel);
                mFailedHabitAdapter.notifyItemInserted(mHabitModelFailedList.size() - 1);
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
    @SuppressLint("LongLogTag")
    public void updateHistoryStatus(HabitModel habitModel, String date, String value) {
        DataLocalManager.getInstance().setUserStateChangeData("true");
        Log.i("Change state data change", DataLocalManager.getInstance().getUserStateChangeData());
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
