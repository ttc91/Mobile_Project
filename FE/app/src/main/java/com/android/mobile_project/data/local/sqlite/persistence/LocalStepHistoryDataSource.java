package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.dao.StepHistoryDAO;
import com.android.mobile_project.data.local.sqlite.entity.db.StepHistoryEntity;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.StepHistoryDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalStepHistoryDataSource extends BaseDataSource implements StepHistoryDataSource {

    private final StepHistoryDAO dao;

    @Inject
    public LocalStepHistoryDataSource(StepHistoryDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(StepHistoryEntity stepHistoryEntity) {
        return dao.insert(stepHistoryEntity);
    }

    @Override
    public Completable delete(StepHistoryEntity stepHistoryEntity) {
        return dao.delete(stepHistoryEntity);
    }

    @Override
    public Completable update(StepHistoryEntity stepHistoryEntity) {
        return dao.update(stepHistoryEntity);
    }

    @Override
    public StepHistoryEntity findOneByDate(String date) {
        return dao.findOneByDate(date);
    }
}
