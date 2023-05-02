package com.android.mobile_project.data.repository;

import com.android.mobile_project.data.local.sqlite.persistence.behavior.UserDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteUserDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

@MyCustomAnnotation.MyScope.ActivityScope
public class UserRepository {

    private final UserDataSource mUserDataSource;

    private final RemoteUserDataSource mRemoteUserDataSource;

    @Inject
    public UserRepository(UserDataSource mUserDataSource, RemoteUserDataSource mRemoteUserDataSource) {
        this.mUserDataSource = mUserDataSource;
        this.mRemoteUserDataSource = mRemoteUserDataSource;
    }

    public UserDataSource getMUserDataSource() {
        return mUserDataSource;
    }

    public RemoteUserDataSource getMRemoteUserDataSource() {
        return mRemoteUserDataSource;
    }

}
