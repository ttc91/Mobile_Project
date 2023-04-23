package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;


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

    public HabitEntity(Long habitId, @NonNull String habitName, String habitLogo, @NonNull Long numOfLongestSteak, Long userId, @NonNull Long dayOfTimeId) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.habitLogo = habitLogo;
        this.numOfLongestSteak = numOfLongestSteak;
        this.userId = userId;
        this.dayOfTimeId = dayOfTimeId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    @NonNull
    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(@NonNull String habitName) {
        this.habitName = habitName;
    }

    public String getHabitLogo() {
        return habitLogo;
    }

    public void setHabitLogo(String habitLogo) {
        this.habitLogo = habitLogo;
    }

    @NonNull
    public Long getNumOfLongestSteak() {
        return numOfLongestSteak;
    }

    public void setNumOfLongestSteak(@NonNull Long numOfLongestSteak) {
        this.numOfLongestSteak = numOfLongestSteak;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @NonNull
    public Long getDayOfTimeId() {
        return dayOfTimeId;
    }

    public void setDayOfTimeId(@NonNull Long dayOfTimeId) {
        this.dayOfTimeId = dayOfTimeId;
    }
}
