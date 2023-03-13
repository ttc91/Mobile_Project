package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitInWeekDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class HabitInWeekRepository {

    private final HabitInWeekDataSource mHabitInWeekDataSource;

    @Inject
    public HabitInWeekRepository(HabitInWeekDataSource mHabitInWeekDataSource) {
        this.mHabitInWeekDataSource = mHabitInWeekDataSource;
    }

    public HabitInWeekDataSource getMHabitInWeekDataSource() {
        return mHabitInWeekDataSource;
    }
}
