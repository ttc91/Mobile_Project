package com.android.mobile_project.data.local.model.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(tableName = "tbl_history")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long historyId;

    @ColumnInfo(name = "date")
    @Nullable
    public Long historyDate;

    @NonNull
    @ColumnInfo(defaultValue = "true")
    public boolean historyHabitsState;

    @ColumnInfo(name = "user_id")
    public Long userId;

    @ColumnInfo(name = "habit_id")
    public Long habitId;

}
