package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity(tableName = "tbl_habit",
    foreignKeys = @ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = UserEntity.class,
            parentColumns = "id",
            childColumns = "user_id"
    ))
public class HabitEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long habitId;

    @ColumnInfo(name = "habit_name")
    @NonNull
    public String habitName;

    @ColumnInfo(name = "habit_logo")
    public String habitLogo;

    @ColumnInfo(name = "longest_steak", defaultValue = "0")
    @NonNull
    public Long numOfLongestSteak;

    /**
     * <b>Foreign key</b>
     */
    @ColumnInfo(name = "user_id", index = true)
    public Long userId;

    @ColumnInfo(name = "day_of_time_id")
    @NonNull
    public Long dayOfTimeId;

}
