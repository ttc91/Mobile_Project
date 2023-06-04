package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.StepHistoryDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class StepHistoryRepository {

    private final StepHistoryDataSource mStepHistoryDataSource;

    @Inject
    public StepHistoryRepository(StepHistoryDataSource mStepHistoryDataSource) {
        this.mStepHistoryDataSource = mStepHistoryDataSource;
    }

    public StepHistoryDataSource getMStepHistoryDataSource() {
        return mStepHistoryDataSource;
    }
}
