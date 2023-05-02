package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;

import io.reactivex.rxjava3.core.Single;

public interface UserDataSource extends BaseDataSource<UserEntity>{

    Single<Long> getUserIdByName(String name);

}
