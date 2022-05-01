package com.android.mobile_project.data.local.model.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.android.mobile_project.data.local.model.BaseEntity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Timer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(primaryKeys = {"user_id", "habit_id", "day_of_week_id"}, tableName = "tbl_habit_in_week")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HabitInWeekEntity extends BaseEntity implements Serializable {

    @ColumnInfo(name = "user_id")
    @NonNull
    public Long userId;

    @ColumnInfo(name = "habit_id")
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

}
