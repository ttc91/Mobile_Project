package com.android.mobile_project.data.local.model.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ColumnInfo(name = "second_time")
    @NonNull
    public Long secondTime;

}
