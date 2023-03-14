package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HistoryDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalHistoryDataSource implements HistoryDataSource {

    private final HistoryDAO dao;

    @Inject
    public LocalHistoryDataSource(HistoryDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(HistoryEntity historyEntity) {
        return dao.insert(historyEntity);
    }

    @Override
    public Completable delete(HistoryEntity historyEntity) {
        return dao.delete(historyEntity);
    }

    @Override
    public Completable update(HistoryEntity historyEntity) {
        return dao.update(historyEntity);
    }

    @Override
    public Flowable<List<HistoryEntity>> getAllHistoryList() {
        return dao.getAllHistoryList();
    }

    @Override
    public Flowable<List<HistoryEntity>> getHistoryListByAndUserIdHabitId(Long habitId, Long userId) {
        return dao.getHistoryListByAndUserIdHabitId(habitId, userId);
    }

    @Override
    public Single<HistoryEntity> getHistoryByHabitIdAndDate(Long hId, String date) {
        return dao.getHistoryByHabitIdAndDate(hId, date);
    }

    @Override
    public Flowable<List<HistoryEntity>> getHistoryByDate(Long uId, String date) {
        return dao.getHistoryByDate(uId, date);
    }
}