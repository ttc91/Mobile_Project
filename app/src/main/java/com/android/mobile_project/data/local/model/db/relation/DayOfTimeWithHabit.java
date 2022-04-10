package com.android.mobile_project.data.local.model.db.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.android.mobile_project.data.local.model.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.model.db.HabitEntity;

public class DayOfTimeWithHabit {

    @Embedded public DayOfTimeEntity dayOfTimeEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "day_of_time_id"
    )

    public HabitEntity habitEntity;
}
