package com.android.mobile_project.data.local.sqlite.entity.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(tableName = "tbl_day_of_time")
public class DayOfTimeEntity extends BaseEntity implements Serializable {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    public Long dayOfTimeId;

    @ColumnInfo(name = "time_name")
    @NonNull
    public String dayOfTimeName;

}
