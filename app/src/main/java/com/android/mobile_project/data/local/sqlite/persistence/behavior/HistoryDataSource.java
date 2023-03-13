package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface HistoryDataSource extends BaseDataSource<HistoryEntity> {

    Flowable<List<HistoryEntity>> getAllHistoryList();

    Flowable<List<HistoryEntity>> getHistoryListByAndUserIdHabitId(Long habitId, Long userId);

    Single<HistoryEntity> getHistoryByHabitIdAndDate(Long hId, String date);

    Flowable<List<HistoryEntity>> getHistoryByDate(Long uId, String date);

}
