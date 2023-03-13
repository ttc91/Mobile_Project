package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.DayOfTimeWithHabit;
import com.android.mobile_project.data.local.sqlite.dao.DayOfTimeDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.DayOfTimeDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalDayOfTimeDataSource implements DayOfTimeDataSource {

    private final DayOfTimeDAO dao;

    @Inject
    public LocalDayOfTimeDataSource(DayOfTimeDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(DayOfTimeEntity dayOfTimeEntity) {
        return dao.insert(dayOfTimeEntity);
    }

    @Override
    public Completable delete(DayOfTimeEntity dayOfTimeEntity) {
        return dao.delete(dayOfTimeEntity);
    }

    @Override
    public Completable update(DayOfTimeEntity dayOfTimeEntity) {
        return dao.update(dayOfTimeEntity);
    }

    @Override
    public Flowable<List<DayOfTimeWithHabit>> getDayOfTimeWithHabits() {
        return dao.getDayOfTimeWithHabits();
    }

    @Override
    public Single<DayOfTimeEntity> searchDayOfTimeByName(String dayOfTimeName) {
        return dao.searchDayOfTimeByName(dayOfTimeName);
    }

    @Override
    public Flowable<List<DayOfTimeEntity>> getDayOfTimeList() {
        return dao.getDayOfTimeList();
    }

    @Override
    public Single<DayOfTimeEntity> getDayOfTimeById(Long id) {
        return dao.getDayOfTimeById(id);
    }

}
