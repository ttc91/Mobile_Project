package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.DayOfTimeWithHabit;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface DayOfTimeDataSource extends BaseDataSource<DayOfTimeEntity>{

    Flowable<List<DayOfTimeWithHabit>> getDayOfTimeWithHabits();

    Single<DayOfTimeEntity> searchDayOfTimeByName(String dayOfTimeName);

    Flowable<List<DayOfTimeEntity>> getDayOfTimeList();

    Single<DayOfTimeEntity> getDayOfTimeById(Long id);

}
