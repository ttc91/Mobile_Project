package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserEntity user);

    @Update
    void updateUser(UserEntity user);

    @Query("SELECT id FROM tbl_user WHERE user_name = :name")
    public int getUserIdByName(String name);

}
