package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitInWeekDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalHabitInWeekDataSource extends BaseDataSource implements HabitInWeekDataSource {

    private final HabitInWeekDAO dao;

    @Inject
    public LocalHabitInWeekDataSource(HabitInWeekDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(HabitInWeekEntity habitInWeekEntity) {
        return subscribeCompletable(dao.insert(habitInWeekEntity));
    }

    @Override
    public Completable delete(HabitInWeekEntity habitInWeekEntity) {
        return subscribeCompletable(dao.delete(habitInWeekEntity));
    }

    @Override
    public Completable update(HabitInWeekEntity habitInWeekEntity) {
        return subscribeCompletable(dao.update(habitInWeekEntity));
    }

    @Override
    public Flowable<List<HabitInWeekEntity>> getDayOfWeekHabitListByUserAndHabitId(Long userId, Long habitId) {
        return subscribeFlowable(dao.getDayOfWeekHabitListByUserAndHabitId(userId, habitId));
    }

    @Override
    public Flowable<List<HabitInWeekEntity>> getHabitInWeekEntityByDayOfWeekId(Long userId, Long id) {
        return subscribeFlowable(dao.getHabitInWeekEntityByDayOfWeekId(userId, id));
    }

    @Override
    public Completable deleteHabitInWeekByHabitId(Long habitId) {
        return dao.deleteHabitInWeekByHabitId(habitId);
    }

    @Override
    public List<HabitInWeekEntity> getHabitInWeekEntityByDayOfWeekIdInBackground(Long userId, Long id) {
        return dao.getHabitInWeekEntityByDayOfWeekIdInBackground(userId, id);
    }

    @Override
    public HabitInWeekEntity getDayOfWeekHabitListByUserAndHabitIdAndId(Long userId, Long habitId, Long id) {
        return dao.getDayOfWeekHabitListByUserAndHabitIdAndId(userId, habitId, id);
    }

    @Override
    public List<HabitInWeekEntity> getAll() {
        return dao.getAll();
    }

    @Override
    public Completable insertAll(HabitInWeekEntity... habitEntities) {
        return dao.insertAll(habitEntities);
    }
    @Override
    public Completable deleteAll() {
        return dao.deleteAll();
    }
}
