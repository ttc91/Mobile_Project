package com.android.mobile_project.ui.activity.main.fragment.planner;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.ui.activity.main.fragment.planner.service.InitService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

@MyCustomAnnotation.MyScope.FragmentScope
public class PlannerViewModel extends ViewModel {

    private final HistoryRepository mHistoryRepository;
    private final HabitRepository mHabitRepository;

    @Inject
    public PlannerViewModel(HistoryRepository mHistoryRepository, HabitRepository mHabitRepository) {
        this.mHistoryRepository = mHistoryRepository;
        this.mHabitRepository = mHabitRepository;
    }

    protected InitService initService;

    protected static Integer steak = 0;

    protected List<HistoryModel> getHistoryByDate(String historyDate){

        final List<HistoryModel>[] historyModels = new List[]{new ArrayList<>()};

        mHistoryRepository.getMHistoryDataSource().getHistoryByDate(DataLocalManager.getUserId(), historyDate)
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableSubscriber<List<HistoryEntity>>() {
                    @SuppressLint("NewApi")
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

    protected List<HabitModel> getHabitListDescByLongestSteak(){

        final List<HabitModel>[] habitModels = new List[]{new ArrayList<>()};

        mHabitRepository.getMHabitDataSource().getHabitListDescByLongestSteak()
                .observeOn(Schedulers.single())
                .subscribeWith(new DisposableSubscriber<List<HabitEntity>>() {
                    @SuppressLint("LongLogTag")
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(List<HabitEntity> habitEntities) {
                        Log.i("getHabitListDescByLongestSteak", "onNext");
                        habitModels[0] = HabitMapper.getInstance().mapToListModel(habitEntities);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable t) {
                        Log.e("getHabitListDescByLongestSteak", "onError", t);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete() {
                        Log.i("getHabitListDescByLongestSteak", "onComplete");
                    }
                });
        return habitModels[0];
    }

}
