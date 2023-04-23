package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;

@Entity(
        primaryKeys = {"user_id", "habit_id", "day_of_week_id"},
        tableName = "tbl_habit_in_week",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = HabitEntity.class,
                        parentColumns = "id",
                        childColumns = "habit_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class HabitInWeekEntity extends BaseEntity implements Serializable {

    @ColumnInfo(name = "user_id", index = true)
    @NonNull
    public Long userId;

    @ColumnInfo(name = "habit_id", index = true)
    @NonNull
    public Long habitId;

    @ColumnInfo(name = "day_of_week_id")
    @NonNull
    public Long dayOfWeekId;

    @ColumnInfo(name = "timer_hour")
    @Nullable
    public Long timerHour;

    @ColumnInfo(name = "timer_minute")
    @Nullable
    public Long timerMinute;

    @ColumnInfo(name = "timer_second")
    @Nullable
    public Long timerSecond;

    public HabitInWeekEntity(@NonNull Long userId, @NonNull Long habitId, @NonNull Long dayOfWeekId, @Nullable Long timerHour, @Nullable Long timerMinute, @Nullable Long timerSecond) {
        this.userId = userId;
        this.habitId = habitId;
        this.dayOfWeekId = dayOfWeekId;
        this.timerHour = timerHour;
        this.timerMinute = timerMinute;
        this.timerSecond = timerSecond;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Long userId) {
        this.userId = userId;
    }

    @NonNull
    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(@NonNull Long habitId) {
        this.habitId = habitId;
    }

    @NonNull
    public Long getDayOfWeekId() {
        return dayOfWeekId;
    }

    public void setDayOfWeekId(@NonNull Long dayOfWeekId) {
        this.dayOfWeekId = dayOfWeekId;
    }

    @Nullable
    public Long getTimerHour() {
        return timerHour;
    }

    public void setTimerHour(@Nullable Long timerHour) {
        this.timerHour = timerHour;
    }

    @Nullable
    public Long getTimerMinute() {
        return timerMinute;
    }

    public void setTimerMinute(@Nullable Long timerMinute) {
        this.timerMinute = timerMinute;
    }

    @Nullable
    public Long getTimerSecond() {
        return timerSecond;
    }

    public void setTimerSecond(@Nullable Long timerSecond) {
        this.timerSecond = timerSecond;
    }
}
