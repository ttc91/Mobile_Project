package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface HabitInWeekDataSource extends BaseDataSource<HabitInWeekEntity> {

    Flowable<List<HabitInWeekEntity>> getDayOfWeekHabitListByUserAndHabitId(Long userId, Long habitId);

    Flowable<List<HabitInWeekEntity>> getHabitInWeekEntityByDayOfWeekId(Long userId, Long id);

    Completable deleteHabitInWeekByHabitId(Long habitId);

}
