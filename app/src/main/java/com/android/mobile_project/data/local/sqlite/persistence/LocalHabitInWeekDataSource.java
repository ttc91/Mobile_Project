package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitInWeekDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalHabitInWeekDataSource implements HabitInWeekDataSource {

    private final HabitInWeekDAO dao;

    @Inject
    public LocalHabitInWeekDataSource(HabitInWeekDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(HabitInWeekEntity habitInWeekEntity) {
        return dao.insert(habitInWeekEntity);
    }

    @Override
    public Completable delete(HabitInWeekEntity habitInWeekEntity) {
        return dao.delete(habitInWeekEntity);
    }

    @Override
    public Completable update(HabitInWeekEntity habitInWeekEntity) {
        return dao.update(habitInWeekEntity);
    }

    @Override
    public Flowable<List<HabitInWeekEntity>> getDayOfWeekHabitListByUserAndHabitId(Long userId, Long habitId) {
        return dao.getDayOfWeekHabitListByUserAndHabitId(userId, habitId);
    }

    @Override
    public Flowable<List<HabitInWeekEntity>> getHabitInWeekEntityByDayOfWeekId(Long id) {
        return dao.getHabitInWeekEntityByDayOfWeekId(id);
    }
}