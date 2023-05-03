package com.android.mobile_project.data.utils.mapper;

import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;
import com.android.mobile_project.data.remote.model.UserModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

public class UserMapper extends BaseMapper<UserEntity, UserModel> implements EntityMapper<UserEntity, UserModel> {

    private static volatile UserMapper INSTANCE;

    public static UserMapper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserMapper();
        }
        return INSTANCE;
    }

    @Override
    public UserModel mapToModel(UserEntity e) {
        return new UserModel( e.userId, e.userName );
    }

    @Override
    public UserEntity mapToEntity(UserModel m) {
        UserEntity entity = new UserEntity();
        entity.userId = m.getUserId();
        entity.userName = m.getUserName();
        return entity;
    }

}
