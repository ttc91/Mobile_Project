package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitInWeekDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteHabitInWeekDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class HabitInWeekRepository {

    private final HabitInWeekDataSource mHabitInWeekDataSource;

    private final RemoteHabitInWeekDataSource mRemoteHabitInWeekDataSource;

    @Inject
    public HabitInWeekRepository(HabitInWeekDataSource mHabitInWeekDataSource, RemoteHabitInWeekDataSource mRemoteHabitInWeekDataSource) {
        this.mHabitInWeekDataSource = mHabitInWeekDataSource;
        this.mRemoteHabitInWeekDataSource = mRemoteHabitInWeekDataSource;
    }

    public HabitInWeekDataSource getMHabitInWeekDataSource() {
        return mHabitInWeekDataSource;
    }

    public RemoteHabitInWeekDataSource getMRemoteHabitInWeekDataSource() {
        return mRemoteHabitInWeekDataSource;
    }
}
