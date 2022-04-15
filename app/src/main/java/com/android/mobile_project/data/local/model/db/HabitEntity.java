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

@Entity(tableName = "tbl_habit")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer habitId;

    @ColumnInfo(name = "habit_name")
    @NonNull
    public String habitName;

    @ColumnInfo(name = "habit_logo")
    public String habitLogo;

    @ColumnInfo(name = "longest_steak", defaultValue = "0")
    @NonNull
    public Long numOfLongestSteak;

    //FK :

    @ColumnInfo(name = "user_id")
    public Long userId;

    @ColumnInfo(name = "day_of_time_id")
    @NonNull
    public Long dayOfTimeId;

}
