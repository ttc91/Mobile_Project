package com.android.mobile_project.data.local.sqlite.entity.db.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;

public class DayOfTimeWithHabit {

    @Embedded public DayOfTimeEntity dayOfTimeEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "day_of_time_id"
    )
    public HabitEntity habitEntity;
}
