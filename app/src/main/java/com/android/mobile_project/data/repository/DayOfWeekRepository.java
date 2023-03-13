package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.DayOfWeekDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class DayOfWeekRepository {

    private final DayOfWeekDataSource mDayOfWeekDataSource;

    @Inject
    public DayOfWeekRepository(DayOfWeekDataSource mDayOfWeekDataSource) {
        this.mDayOfWeekDataSource = mDayOfWeekDataSource;
    }

    public DayOfWeekDataSource getMDayOfWeekDataSource() {
        return mDayOfWeekDataSource;
    }
}
