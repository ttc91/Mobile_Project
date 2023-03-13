package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface DayOfWeekDataSource extends BaseDataSource<DayOfWeekEntity>{

    Single<DayOfWeekEntity> searchDayOfWeekByName(String dayOfWeekName);

    Flowable<List<DayOfWeekEntity>> getDayOfWeekList();

    Single<DayOfWeekEntity> getDayOfWeekById(Long id);

}
