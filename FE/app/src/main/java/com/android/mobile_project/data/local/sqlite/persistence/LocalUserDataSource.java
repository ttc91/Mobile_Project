package com.android.mobile_project.data.local.sqlite.persistence;

import com.android.mobile_project.data.local.sqlite.dao.UserDAO;
import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.UserDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@MyCustomAnnotation.MyScope.ActivityScope
public class LocalUserDataSource implements UserDataSource {

    private final UserDAO dao;

    @Inject
    public LocalUserDataSource(UserDAO dao) {
        this.dao = dao;
    }

    @Override
    public Completable insert(UserEntity userEntity) {
        return dao.insert(userEntity);
    }

    @Override
    public Completable delete(UserEntity userEntity) {
        return dao.delete(userEntity);
    }

    @Override
    public Completable update(UserEntity userEntity) {
        return dao.update(userEntity);
    }

    @Override
    public Single<Long> getUserIdByName(String name) {
        return dao.getUserIdByName(name);
    }

}
