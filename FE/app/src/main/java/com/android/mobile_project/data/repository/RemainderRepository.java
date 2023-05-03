package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.RemainderDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemainderRepository {

    private RemainderDataSource mRemainderDataSource;

    @Inject
    public RemainderRepository(RemainderDataSource mRemainderDataSource) {
        this.mRemainderDataSource = mRemainderDataSource;
    }

    public RemainderDataSource getMRemainderDataSource() {
        return mRemainderDataSource;
    }

}
