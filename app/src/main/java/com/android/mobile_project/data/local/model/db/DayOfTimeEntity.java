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

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "tbl_day_of_time")
public class DayOfTimeEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long dayOfTimeId;

    @ColumnInfo(name = "time_name")
    @NonNull
    public String dayOfTimeName;

}
