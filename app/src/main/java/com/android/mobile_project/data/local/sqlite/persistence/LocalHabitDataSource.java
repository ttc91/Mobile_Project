package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalHabitDataSource extends BaseDataSource implements HabitDataSource {

    private final HabitDAO dao;

    @Inject
    public LocalHabitDataSource(HabitDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(HabitEntity habitEntity) {
        return subscribeCompletable(dao.insert(habitEntity));
    }

    @Override
    public Completable delete(HabitEntity habitEntity) {
        return subscribeCompletable(dao.delete(habitEntity));
    }

    @Override
    public Completable update(HabitEntity habitEntity) {
        return subscribeCompletable(dao.update(habitEntity));
    }

    @Override
    public Completable deleteHabitById(Long id) {
        return subscribeCompletable(dao.deleteHabitById(id));
    }

    @Override
    public Flowable<List<HabitEntity>> getHabitListByUserAndDayOfTime(Long userId, Long dayOfTimeId) {
        return subscribeFlowable(dao.getHabitListByUserAndDayOfTime(userId, dayOfTimeId));
    }

    @Override
    public Single<HabitEntity> getHabitByName(String name) {
        return subscribeSingle(dao.getHabitByName(name));
    }

    @Override
    public Flowable<List<HabitEntity>> getHabitListByUserId(Long id) {
        return subscribeFlowable(dao.getHabitListByUserId(id));
    }

    @Override
    public Single<HabitEntity> getHabitByUserIdAndHabitId(Long userId, Long habitId) {
        return subscribeSingle(dao.getHabitByUserIdAndHabitId(userId, habitId));
    }

    @Override
    public Completable updateDateOfTimeInHabit(Long dayOfTimeId, Long habitId) {
        return subscribeCompletable(dao.updateDateOfTimeInHabit(dayOfTimeId, habitId));
    }

    @Override
    public Completable updateNameOfHabit(String hName, Long habitId) {
        return subscribeCompletable(dao.updateNameOfHabit(hName, habitId));
    }

    @Override
    public Flowable<List<HabitEntity>> getHabitListDescByLongestSteak() {
        return subscribeFlowable(dao.getHabitListDescByLongestSteak());
    }

    @Override
    public HabitEntity getHabitByUserIdAndHabitIdInBackground(Long userId, Long habitId) {
        return dao.getHabitByUserIdAndHabitIdInBackground(userId, habitId);
    }

    @Override
    public HabitEntity getFinalHabitByUserIdInBackground(Long userId) {
        return dao.getFinalHabitByUserIdInBackground(userId);
    }
}
