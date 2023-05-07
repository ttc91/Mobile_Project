package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface RemainderDataSource extends BaseDataSource<RemainderEntity> {

    Flowable<List<RemainderEntity>> getRemainderListByHabitId (Long habitId);

    Completable deleteAllRemainderByHabitId(Long habitId);

    Single<RemainderEntity> checkExistRemainder(Long h, Long m, Long id);

    Completable deleteRemainderByTimerHourAndTimerMinutesAndId(Long h, Long m, Long id);

    List<RemainderEntity> getAll();

    Completable insertAll(RemainderEntity... habitEntities);

    Completable deleteAll();

}
