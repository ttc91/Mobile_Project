package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;
import com.android.mobile_project.data.local.sqlite.dao.RemainderDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.RemainderDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalRemainderDataSource implements RemainderDataSource {

    private final RemainderDAO dao;

    @Inject
    public LocalRemainderDataSource(RemainderDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(RemainderEntity remainderEntity) {
        return dao.insert(remainderEntity);
    }

    @Override
    public Completable delete(RemainderEntity remainderEntity) {
        return dao.delete(remainderEntity);
    }

    @Override
    public Completable update(RemainderEntity remainderEntity) {
        return dao.update(remainderEntity);
    }

    @Override
    public Flowable<List<RemainderEntity>> getRemainderListByHabitId(Long habitId) {
        return dao.getRemainderListByHabitId(habitId);
    }

    @Override
    public Completable deleteAllRemainderByHabitId(Long habitId) {
        return dao.deleteAllRemainderByHabitId(habitId);
    }

    @Override
    public Single<RemainderEntity> checkExistRemainder(Long h, Long m, Long id) {
        return dao.checkExistRemainder(h, m, id);
    }

    @Override
    public Completable deleteRemainderByTimerHourAndTimerMinutesAndId(Long h, Long m, Long id) {
        return dao.deleteRemainderByTimerHourAndTimerMinutesAndId(h, m, id);
    }

    @Override
    public List<RemainderEntity> getAll() {
        return dao.getAll();
    }
}
