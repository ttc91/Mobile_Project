package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface HabitDataSource extends BaseDataSource<HabitEntity>{

    Completable deleteHabitById(Long id);

    Flowable<List<HabitEntity>> getHabitListByUserAndDayOfTime(Long userId, Long dayOfTimeId);

    Single<HabitEntity> getHabitByName(String name);

    Flowable<List<HabitEntity>> getHabitListByUserId(Long id);

    Single<HabitEntity> getHabitByUserIdAndHabitId(Long userId, Long habitId);

    Completable updateDateOfTimeInHabit(Long dayOfTimeId, Long habitId);

    Completable updateNameOfHabit(String hName, Long habitId);

    Single<HabitEntity> getHabitByMostLongestSteak();

    HabitEntity getHabitByUserIdAndHabitIdInBackground(Long userId, Long habitId);

    HabitEntity getFinalHabitByUserIdInBackground(Long userId);

    void updateHabitInBackground(HabitEntity entity);

    List<HabitEntity> getAll();

}
