package com.android.mobile_project.data.local.sqlite.entity.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;


@Entity(tableName = "tbl_day_of_time")
public class DayOfTimeEntity extends BaseEntity implements Serializable {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    public Long dayOfTimeId;

    @ColumnInfo(name = "time_name")
    @NonNull
    public String dayOfTimeName;

    public DayOfTimeEntity(Long dayOfTimeId, @NonNull String dayOfTimeName) {
        this.dayOfTimeId = dayOfTimeId;
        this.dayOfTimeName = dayOfTimeName;
    }
}
