package com.android.mobile_project.data.local.model.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_history")
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long historyId;

    @ColumnInfo(name = "date")
    @Nullable
    private Long historyDate;

    @NonNull
    @ColumnInfo(defaultValue = "true")
    private boolean historyHabitsState;

    @ColumnInfo(name = "user_id")
    private Long userId;

    @ColumnInfo(name = "habit_id")
    private Long habitId;

}
