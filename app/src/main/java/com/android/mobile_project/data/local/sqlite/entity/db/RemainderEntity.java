package com.android.mobile_project.data.local.sqlite.entity.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "tbl_remainder",
        foreignKeys = {
                @ForeignKey(
                        entity = HabitEntity.class,
                        parentColumns = "id",
                        childColumns = "habit_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class RemainderEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    public Long remainderId;

    @ColumnInfo(name = "habit_id", index = true)
    @NonNull
    public Long habitId;

    @ColumnInfo(name = "hour_time")
    @NonNull
    public Long hourTime;

    @ColumnInfo(name = "minutes_time")
    @NonNull
    public Long minutesTime;

}
