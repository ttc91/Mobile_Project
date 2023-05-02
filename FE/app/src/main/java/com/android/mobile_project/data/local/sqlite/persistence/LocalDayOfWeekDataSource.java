package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.dao.DayOfWeekDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.DayOfWeekDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalDayOfWeekDataSource implements DayOfWeekDataSource {

    private final DayOfWeekDAO dao;

    @Inject
    public LocalDayOfWeekDataSource(DayOfWeekDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(DayOfWeekEntity dayOfWeekEntity) {
        return dao.insert(dayOfWeekEntity);
    }

    @Override
    public Completable delete(DayOfWeekEntity dayOfWeekEntity) {
        return dao.delete(dayOfWeekEntity);
    }

    @Override
    public Completable update(DayOfWeekEntity dayOfWeekEntity) {
        return dao.update(dayOfWeekEntity);
    }

    @Override
    public Single<DayOfWeekEntity> searchDayOfWeekByName(String dayOfWeekName) {
        return dao.searchDayOfWeekByName(dayOfWeekName);
    }

    @Override
    public Flowable<List<DayOfWeekEntity>> getDayOfWeekList() {
        return dao.getDayOfWeekList();
    }

    @Override
    public Single<DayOfWeekEntity> getDayOfWeekById(Long id) {
        return dao.getDayOfWeekById(id);
    }
}
