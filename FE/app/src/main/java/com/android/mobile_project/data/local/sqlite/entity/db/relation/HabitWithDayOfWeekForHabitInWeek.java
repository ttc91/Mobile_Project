package com.android.mobile_project.data.local.sqlite.entity.db.relation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;

import java.util.List;

public class HabitWithDayOfWeekForHabitInWeek {

    @Embedded
    public HabitEntity habitEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = HabitInWeekEntity.class,
                    parentColumn = "habit_id",
                    entityColumn = "day_of_week_id"
            )
    )
    public List<DayOfWeekEntity> dayOfWeekEntityList;

}
