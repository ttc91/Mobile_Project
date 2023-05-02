package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.RemainderDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteRemainderDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemainderRepository {

    private final RemainderDataSource mRemainderDataSource;

    private final RemoteRemainderDataSource mRemoteRemainderDataSource;

    @Inject
    public RemainderRepository(RemainderDataSource mRemainderDataSource, RemoteRemainderDataSource mRemoteRemainderDataSource) {
        this.mRemainderDataSource = mRemainderDataSource;
        this.mRemoteRemainderDataSource = mRemoteRemainderDataSource;
    }

    public RemainderDataSource getMRemainderDataSource() {
        return mRemainderDataSource;
    }

    public RemoteRemainderDataSource getMRemoteRemainderDataSource() {
        return mRemoteRemainderDataSource;
    }
}
