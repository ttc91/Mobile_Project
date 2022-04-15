package com.android.mobile_project.data.local.model.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.model.BaseEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(tableName = "tbl_user")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Data
public class UserEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long userId;

    @ColumnInfo(name = "user_name")
    @NonNull
    public String userName;



}
