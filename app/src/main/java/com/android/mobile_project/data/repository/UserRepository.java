package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.UserDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class UserRepository {

    private UserDataSource mUserDataSource;

    @Inject
    public UserRepository(UserDataSource mUserDataSource) {
        this.mUserDataSource = mUserDataSource;
    }

    public UserDataSource getMUserDataSource() {
        return mUserDataSource;
    }

}
