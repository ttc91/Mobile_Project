package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteHabitDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class HabitRepository {

    private final HabitDataSource mHabitDataSource;

    private final RemoteHabitDataSource mRemoteHabitDataSource;

    @Inject
    public HabitRepository(HabitDataSource mHabitDataSource, RemoteHabitDataSource mRemoteHabitDataSource) {
        this.mHabitDataSource = mHabitDataSource;
        this.mRemoteHabitDataSource = mRemoteHabitDataSource;
    }

    public HabitDataSource getMHabitDataSource() {
        return mHabitDataSource;
    }

    public RemoteHabitDataSource getMRemoteHabitDataSource() {
        return mRemoteHabitDataSource;
    }

}
