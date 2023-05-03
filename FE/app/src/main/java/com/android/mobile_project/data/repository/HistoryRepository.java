package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.HistoryDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteHistoryDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class HistoryRepository {

    private final HistoryDataSource mHistoryDataSource;

    private final RemoteHistoryDataSource mRemoteHistoryDataSource;

    @Inject
    public HistoryRepository(HistoryDataSource mHistoryDataSource, RemoteHistoryDataSource mRemoteHistoryDataSource) {
        this.mHistoryDataSource = mHistoryDataSource;
        this.mRemoteHistoryDataSource = mRemoteHistoryDataSource;
    }

    public HistoryDataSource getMHistoryDataSource() {
        return mHistoryDataSource;
    }

    public RemoteHistoryDataSource getMRemoteHistoryDataSource() {
        return mRemoteHistoryDataSource;
    }

}
