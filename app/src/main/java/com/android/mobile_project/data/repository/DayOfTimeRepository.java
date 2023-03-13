package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.DayOfTimeDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class DayOfTimeRepository {

    private final DayOfTimeDataSource mDayOfTimeDataSource;

    @Inject
    public DayOfTimeRepository(DayOfTimeDataSource mDayOfTimeDataSource) {
        this.mDayOfTimeDataSource = mDayOfTimeDataSource;
    }

    public DayOfTimeDataSource getMDayOfTimeDataSource() {
        return mDayOfTimeDataSource;
    }
}
