package com.android.mobile_project.data.local.model.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "tbl_remainder")
public class RemainderEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long remainderId;

    @ColumnInfo(name = "habit_id")
    @NonNull
    private Long habitId;

    @ColumnInfo(name = "remainder_time")
    @NonNull
    private Long remainderTime;

}
