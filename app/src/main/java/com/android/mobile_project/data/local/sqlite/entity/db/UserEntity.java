package com.android.mobile_project.data.local.sqlite.entity.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;

@Entity(tableName = "tbl_user")
public class UserEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long userId;

    @ColumnInfo(name = "user_name")
    @NonNull
    public String userName;

}
