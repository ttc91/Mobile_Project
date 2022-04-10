package com.android.mobile_project.data.local.model.db.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;

public class HabitWithHistory {

    @Embedded public HabitEntity habitEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "habit_id"
    )

    public HistoryEntity historyEntity;

}
