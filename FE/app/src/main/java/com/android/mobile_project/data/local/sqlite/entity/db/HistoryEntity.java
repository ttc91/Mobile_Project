package com.android.mobile_project.data.local.sqlite.entity.db;

import androidx.annotation.Nullable;
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

}
