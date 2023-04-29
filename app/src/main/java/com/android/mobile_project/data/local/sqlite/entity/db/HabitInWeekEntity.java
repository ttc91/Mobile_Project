package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

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

}
