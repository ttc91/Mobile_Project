package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.android.mobile_project.data.local.sqlite.entity.BaseEntity;

import java.io.Serializable;


@Entity(
        tableName = "tbl_history",
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
public class HistoryEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long historyId;

    @ColumnInfo(name = "date")
    @Nullable
    public String historyDate;

    @ColumnInfo(name = "state", defaultValue = "null")
    @Nullable
    public String historyHabitsState;

    /**
     * <b>Foreign key</b>
     */
    @ColumnInfo(name = "user_id", index = true)
    public Long userId;

    @ColumnInfo(name = "habit_id", index = true)
    public Long habitId;

    public HistoryEntity() {
    }

    public HistoryEntity(Long historyId, @Nullable String historyDate, @Nullable String historyHabitsState, Long userId, Long habitId) {
        this.historyId = historyId;
        this.historyDate = historyDate;
        this.historyHabitsState = historyHabitsState;
        this.userId = userId;
        this.habitId = habitId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    @Nullable
    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(@Nullable String historyDate) {
        this.historyDate = historyDate;
    }

    @Nullable
    public String getHistoryHabitsState() {
        return historyHabitsState;
    }

    public void setHistoryHabitsState(@Nullable String historyHabitsState) {
        this.historyHabitsState = historyHabitsState;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }
}
