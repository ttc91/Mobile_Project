package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class HabitRepository {

    private final HabitDataSource mHabitDataSource;

    @Inject
    public HabitRepository(HabitDataSource mHabitDataSource) {
        this.mHabitDataSource = mHabitDataSource;
    }

    public HabitDataSource getMHabitDataSource() {
        return mHabitDataSource;
    }
}
