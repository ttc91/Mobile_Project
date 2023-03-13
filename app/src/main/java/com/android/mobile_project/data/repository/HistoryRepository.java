package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.HistoryDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class HistoryRepository {

    private final HistoryDataSource mHistoryDataSource;

    @Inject
    public HistoryRepository(HistoryDataSource mHistoryDataSource) {
        this.mHistoryDataSource = mHistoryDataSource;
    }

    public HistoryDataSource getMHistoryDataSource() {
        return mHistoryDataSource;
    }

}
