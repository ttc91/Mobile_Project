package com.android.mobile_project.ui.activity.main.fragment.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
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
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.AfterAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.BeforeAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.DbService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.UpdateService;

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

    private static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final String VAL_TRUE = "true";

    private static final String VAL_FALSE = "false";

    private static final String VAL_NULL = "null";

    protected InitUIService initUIService;
    protected UpdateService updateService;
    protected HabitAdapter.RecyclerViewClickListener recyclerViewClickListener;
    protected InitUIService.InitHabitListUI initHabitListUI;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private DbService.InsertHistoryResult mInsertHistoryResult = new DbService.InsertHistoryResult() {
        @Override
        public void onInsertHistorySuccess(CompositeDisposable disposable) {
            Log.i("mInsertHistoryResult", "onInsertHistorySuccess");
            disposable.clear();
        }

        @Override
        public void onInsertHistoryFailure(CompositeDisposable disposable) {
            Log.i("mInsertHistoryResult", "onInsertHistoryFailure");
            disposable.clear();
        }
    };

    private List<HabitInWeekModel> habitInWeekModelList = new ArrayList<>();

    private MutableLiveData<List<HabitModel>> habitModelListMutableLiveData = new MutableLiveData<>();
    private List<HabitModel> habitModelList = new ArrayList<>();
    private HabitAdapter adapter;

    private MutableLiveData<List<HabitModel>> habitModelBeforeListMutableLiveData = new MutableLiveData<>();
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

    private MutableLiveData<List<HistoryModel>> historyModelListMutableLiveData = new MutableLiveData<>();

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
        text.setText(timeUtils.getDateFromLocalDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void setMonth(TextView text){
        text.setText(timeUtils.getMonthYearFromDate());
    }

    protected LiveData<List<HabitModel>> getHabitModelBeforeListMutableLiveData(){
        return habitModelBeforeListMutableLiveData;
    }

    protected LiveData<List<HabitModel>> getHabitModelAfterListMutableLiveData(){
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

    protected LiveData<List<HistoryModel>> getHistoryModelListLiveData(){
        return historyModelListMutableLiveData;
    }

    protected LiveData<List<HabitModel>> getHabitModelListLiveData (){
        return habitModelListMutableLiveData;
    }

    protected LiveData<List<HabitModel>> getHabitModelDoneListLiveData (){
        return habitModelDoneListMutableLiveData;
    }

    protected LiveData<List<HabitModel>> getHabitModelFailedListLiveData (){
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
        this.dayOfWeekId = timeUtils.getCurrentDayOfWeekId(this.dayName);
    }

    @SuppressLint("LongLogTag")
    protected void getHabitInWeekModels(Long dateOfWeekId, DbService.GetHabitInWeekListResult callback){
        mCompositeDisposable.add(
                mHabitInWeekRepository.getMHabitInWeekDataSource().getHabitInWeekEntityByDayOfWeekId(DataLocalManager.getInstance().getUserId(), dateOfWeekId)
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.single())
                        .subscribe(habitInWeekEntities -> {
                                    Log.i("getHabitInWeekEntityByDayOfWeekId","onNext");
                                    habitInWeekModelList = HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities);
                                    callback.onGetHabitInWeekListSuccess(habitInWeekEntities.size(),
                                            HabitInWeekMapper.getInstance().mapToListModel(habitInWeekEntities),
                                            mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitInWeekEntityByDayOfWeekId", "onError", throwable);
                                    callback.onGetHabitInWeekListFailure(mCompositeDisposable);
                                }
                        )
        );
    }

    @SuppressLint("LongLogTag")
    protected void getHabitsWhenClickDailyCalendar(String date, DbService.GetHabitsWhenClickDailyCalendarResult callback){

        if(LocalDate.parse(date).equals(timeUtils.getSelectedDate())){
            habitModelBeforeList.clear();
            beforeAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
            habitModelBeforeListMutableLiveData.postValue(habitModelBeforeList);
            habitModelAfterList.clear();
            afterAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
            habitModelAfterListMutableLiveData.postValue(habitModelAfterList);
        }else {
            if(LocalDate.parse(date).isBefore(timeUtils.getSelectedDate())){
                mCompositeDisposable.add(
                        mHistoryRepository.getMHistoryDataSource().getHistoryByDate(DataLocalManager.getInstance().getUserId(), date)
                                .observeOn(Schedulers.io())
                                .subscribeOn(Schedulers.single())
                                .subscribe(historyEntityList -> {
                                            Log.i("getHabitDifferenceFromCurrentDay","onNext");
                                            callback.onGetHabitsWhenClickDailyCalendarSuccess(mCompositeDisposable);
                                            getHabitByUserIdAndHabitId(HistoryMapper.getInstance().mapToListModel(historyEntityList),
                                                    false,
                                                    new DbService.GetHabitByUserIdAndHabitIdResult() {
                                                        @Override
                                                        public void onGetHabitByUserIdAndHabitIdSuccess(HabitModel model, CompositeDisposable disposable) {
                                                            Log.i("GetHabitByUserIdAndHabitIdResult", "onGetHabitByUserIdAndHabitIdSuccess");
                                                            habitModelBeforeList.add(model);
                                                            disposable.clear();
                                                        }

                                                        @Override
                                                        public void onGetHabitByUserIdAndHabitIdFailure(CompositeDisposable disposable) {
                                                            Log.e("GetHabitByUserIdAndHabitIdResult", "onGetHabitByUserIdAndHabitIdFailure");
                                                            disposable.clear();
                                                        }
                                                    });
                                            habitModelBeforeListMutableLiveData.postValue(habitModelBeforeList);
                                        }, throwable -> {
                                            Log.e("getHabitDifferenceFromCurrentDay", "onError", throwable);
                                            callback.onGetHabitsWhenClickDailyCalendarFailure(mCompositeDisposable);
                                        }
                                )
                );
            }else{
                Long id = timeUtils.getDayOfWeekId(date);
                getHabitInWeekModels(id, new DbService.GetHabitInWeekListResult() {
                    @Override
                    public void onGetHabitInWeekListSuccess(int size, List<HabitInWeekModel> models, CompositeDisposable disposable) {
                        Log.i("getHabitInWeekModels","onGetHabitInWeekListSuccess");
                        for(HabitInWeekModel model : models){
                            getHabitByUserIdAndHabitId(model.getHabitId(),
                                    new DbService.GetHabitByUserIdAndHabitIdResult() {
                                        @Override
                                        public void onGetHabitByUserIdAndHabitIdSuccess(HabitModel model, CompositeDisposable disposable) {
                                            Log.i("GetHabitByUserIdAndHabitIdResult","onGetHabitByUserIdAndHabitIdSuccess");
                                            habitModelAfterList.add(model);
                                            afterAdapter.notifyItemInserted(habitModelAfterList.size() - 1);
                                            disposable.clear();
                                        }

                                        @Override
                                        public void onGetHabitByUserIdAndHabitIdFailure(CompositeDisposable disposable) {
                                            Log.e("GetHabitByUserIdAndHabitIdResult","onGetHabitByUserIdAndHabitIdFailure");
                                            disposable.clear();
                                        }
                                    });
                        }
                        habitModelAfterListMutableLiveData.postValue(habitModelAfterList);
                        disposable.clear();
                    }

                    @Override
                    public void onGetHabitInWeekListFailure(CompositeDisposable disposable) {
                        Log.e("getHabitInWeekModels", "onGetHabitInWeekListFailure");
                        disposable.clear();
                    }
                });
            }
        }
    }


    @SuppressLint("LongLogTag")
    protected void getHabitByUserIdAndHabitId(List<HistoryModel> models, boolean isCurrentDate, DbService.GetHabitByUserIdAndHabitIdResult callback){

        for(HistoryModel model : models){
            if(model.getHistoryHabitsState().equals(VAL_NULL)){
                mCompositeDisposable.add(
                        mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), model.getHabitId())
                                .observeOn(Schedulers.single())
                                .subscribeOn(Schedulers.io())
                                .subscribe(habitEntity -> {
                                    Log.i("getHabitByUserIdAndHabitId","onSuccess");
                                    if(isCurrentDate){
                                        habitModelList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                                    }
                                    callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                                    callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                                }
                        )
                );
            }else if(model.getHistoryHabitsState().equals(VAL_TRUE)){
                mCompositeDisposable.add(
                        mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), model.getHabitId())
                                .observeOn(Schedulers.single())
                                .subscribeOn(Schedulers.io())
                                .subscribe(habitEntity -> {
                                    Log.i("getHabitByUserIdAndHabitId","onSuccess");
                                    if(isCurrentDate){
                                        habitModelDoneList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                                    }
                                    callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                                    callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                                }
                        )
                );
            }else {
                mCompositeDisposable.add(
                        mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), model.getHabitId())
                                .observeOn(Schedulers.single())
                                .subscribeOn(Schedulers.io())
                                .subscribe(habitEntity -> {
                                    Log.i("getHabitByUserIdAndHabitId","onSuccess");
                                    if(isCurrentDate){
                                        habitModelFailedList.add(HabitMapper.getInstance().mapToModel(habitEntity));
                                    }
                                    callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                                    callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                                }
                        )
                );
            }

        }

        habitModelListMutableLiveData.postValue(habitModelList);
        habitModelDoneListMutableLiveData.postValue(habitModelDoneList);
        habitModelFailedListMutableLiveData.postValue(habitModelFailedList);

    }

    protected void getHistoryByDate(String historyTime, DbService.GetHistoryByDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().getHistoryByDate(DataLocalManager.getInstance().getUserId(), historyTime)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.single())
                .subscribe(historyEntityList -> {
                    Log.i("getHistoryByDate","onNext");

                    if(historyTime.equals(LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT)))){
                        if(historyEntityList.size() == 0){
                            List<HistoryModel> historyModels = new ArrayList<>();
                            HistoryModel historyModel = new HistoryModel();
                            for(HabitInWeekModel model : habitInWeekModelList){

                                historyModel.setHistoryDate(historyTime);
                                historyModel.setUserId(DataLocalManager.getInstance().getUserId());
                                historyModel.setHistoryHabitsState(VAL_NULL);
                                historyModel.setHabitId(model.getHabitId());

                                insertHistory(historyModel, mInsertHistoryResult);
                                historyModels.add(historyModel);
                            }
                            historyModelListMutableLiveData.postValue(historyModels);
                            return;
                        }
                    }

                    historyModelListMutableLiveData.postValue(HistoryMapper.getInstance().mapToListModel(historyEntityList));
                    callback.onGetHistoryByDateSuccess(mCompositeDisposable);

                }, throwable -> {
                    Log.e("getHistoryByDate", "onError", throwable);
                    callback.onGetHistoryByDateFailure(mCompositeDisposable);
                })
        );
    }

    protected void insertHistory(HistoryModel historyModel, DbService.InsertHistoryResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource()
                        .insert(HistoryMapper.getInstance().mapToEntity(historyModel))
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("insertHistory","onComplete");
                            callback.onInsertHistorySuccess(mCompositeDisposable);
                        }, throwable -> {
                            Log.e("insertHistory", "onError", throwable);
                            callback.onInsertHistoryFailure(mCompositeDisposable);
                        }
                )
        );

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

    @SuppressLint("LongLogTag")
    protected void updateHistory(int position, Class<?> adapterName, String value, DbService.UpdateHistoryResult callback){

        final HabitModel habitModel;

        if (HabitAdapter.class.equals(adapterName)) {
            habitModel = habitModelList.get(position);
            habitModelList.remove(position);
            adapter.notifyItemRemoved(position);
            habitModelListMutableLiveData.postValue(habitModelList);
        }else if(DoneHabitAdapter.class.equals(adapterName)){
            habitModel = habitModelDoneList.get(position);
            habitModelDoneList.remove(position);
            doneHabitAdapter.notifyItemRemoved(position);
            habitModelDoneListMutableLiveData.postValue(habitModelDoneList);
        }else {
            habitModel = habitModelFailedList.get(position);
            habitModelFailedList.remove(position);
            failedHabitAdapter.notifyItemRemoved(position);
            habitModelFailedListMutableLiveData.postValue(habitModelFailedList);
        }

        getHistoryByHabitIdAndDate(habitModel.getHabitId(),
                LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT)),
                new DbService.GetHistoryByHabitIdAndDateResult() {
            @Override
            public void onGetHistoryByHabitIdAndDateSuccess(HistoryModel model) {
                Log.i("getHistoryByHabitIdAndDate with ID", String.valueOf(model.getHistoryId()));
                model.setHistoryHabitsState(value);
                mCompositeDisposable.add(
                        mHistoryRepository.getMHistoryDataSource().update(HistoryMapper.getInstance().mapToEntity(model))
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.single())
                                .subscribe(() -> {
                                    Log.i("updateHistory", "onComplete");
                                    callback.onUpdateHistorySuccess(mCompositeDisposable);
                                }, throwable -> {
                                    Log.e("updateHistory", "onError", throwable);
                                    callback.onUpdateHistoryFailure(mCompositeDisposable);
                                }
                        )
                );

                switch (value){
                    case VAL_NULL:
                        habitModelList.add(habitModel);
                        adapter.notifyItemInserted(habitModelList.size() - 1);
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

            }
            @Override
            public void onGetHistoryByHabitIdAndDateFailure() {
                Log.e("getHistoryByHabitIdAndDate with ID", "onGetHistoryByHabitIdAndDateFailure");
            }
        });

    }

    @SuppressLint("LongLogTag")
    @Override
    public void getHistoryByHabitIdAndDate(Long id, String date, DbService.GetHistoryByHabitIdAndDateResult callback){
        mCompositeDisposable.add(
                mHistoryRepository.getMHistoryDataSource().getHistoryByHabitIdAndDate(id, date)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(historyEntity -> {
                            Log.i("getHistoryByHabitIdAndDate", "onSuccess");
                            callback.onGetHistoryByHabitIdAndDateSuccess(HistoryMapper.getInstance().mapToModel(historyEntity));
                        }, throwable -> {
                            Log.e("getHistoryByHabitIdAndDate", "onError", throwable);
                            callback.onGetHistoryByHabitIdAndDateFailure();
                        }
                )
        );
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

    @SuppressLint("LongLogTag")
    protected void getHabitByUserIdAndHabitId(Long habitId, DbService.GetHabitByUserIdAndHabitIdResult callback){

        mCompositeDisposable.add(
                mHabitRepository.getMHabitDataSource().getHabitByUserIdAndHabitId(DataLocalManager.getInstance().getUserId(), habitId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(habitEntity -> {
                            Log.i("getHabitByUserIdAndHabitId", "onSuccess");
                            callback.onGetHabitByUserIdAndHabitIdSuccess(HabitMapper.getInstance().mapToModel(habitEntity), mCompositeDisposable);
                        }, throwable -> {
                            Log.e("getHabitByUserIdAndHabitId", "onError", throwable);
                            callback.onGetHabitByUserIdAndHabitIdFailure(mCompositeDisposable);
                        }
                )
        );
    }

}
