package com.android.mobile_project.data.local.sqlite.entity.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_remainder")
public class RemainderEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long remainderId;

    @ColumnInfo(name = "habit_id")
    @NonNull
    public Long habitId;

    @ColumnInfo(name = "hour_time")
    @NonNull
    public Long hourTime;

    @ColumnInfo(name = "minutes_time")
    @NonNull
    public Long minutesTime;

}
