package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;



@Entity(tableName = "tbl_day_of_week")
public class DayOfWeekEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long dayOfWeekId;

    @ColumnInfo(name = "day_of_week_name")
    @NonNull
    public String dayOfWeekName;

    public DayOfWeekEntity(Long dayOfWeekId, @NonNull String dayOfWeekName) {
        this.dayOfWeekId = dayOfWeekId;
        this.dayOfWeekName = dayOfWeekName;
    }
}
