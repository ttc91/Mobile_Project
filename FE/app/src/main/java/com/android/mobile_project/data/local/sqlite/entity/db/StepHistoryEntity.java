package com.android.mobile_project.data.local.sqlite.entity.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

@Entity(tableName = "tbl_step_history")
public class StepHistoryEntity extends BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long stepHistoryId;

    @ColumnInfo(name = "step_value")
    public Long stepValue;

    @ColumnInfo(name = "step_history_date")
    public String stepHistoryDate;

    /**
     * <b>Foreign key</b>
     */
    @ColumnInfo(name = "user_id", index = true)
    public Long userId;

}
