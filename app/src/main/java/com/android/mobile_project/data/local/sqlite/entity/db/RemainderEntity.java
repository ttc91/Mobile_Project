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

    public RemainderEntity(Long remainderId, @NonNull Long habitId, @NonNull Long hourTime, @NonNull Long minutesTime) {
        this.remainderId = remainderId;
        this.habitId = habitId;
        this.hourTime = hourTime;
        this.minutesTime = minutesTime;
    }

    public RemainderEntity() {
    }

    public Long getRemainderId() {
        return remainderId;
    }

    public void setRemainderId(Long remainderId) {
        this.remainderId = remainderId;
    }

    @NonNull
    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(@NonNull Long habitId) {
        this.habitId = habitId;
    }

    @NonNull
    public Long getHourTime() {
        return hourTime;
    }

    public void setHourTime(@NonNull Long hourTime) {
        this.hourTime = hourTime;
    }

    @NonNull
    public Long getMinutesTime() {
        return minutesTime;
    }

    public void setMinutesTime(@NonNull Long minutesTime) {
        this.minutesTime = minutesTime;
    }
}
