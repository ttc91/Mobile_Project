package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Insert;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.UserEntity;

public interface UserDao {

    @Insert
    void insertUser(UserEntity user);

    @Update
    void updateUser(UserEntity user);

}
