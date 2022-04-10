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

@Entity(tableName = "tbl_day_of_week")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayOfWeekEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long dayOfWeekId;

    @ColumnInfo(name = "day_of_week_name")
    @NonNull
    private String dayOfWeekName;

}
