package com.android.mobile_project.data.local.sqlite.entity.db.relation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;

import java.util.List;

public class HabitWithUserForHabitInWeek {

    @Embedded
    public HabitEntity habitEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = HabitInWeekEntity.class,
                    parentColumn = "habit_id",
                    entityColumn = "user_id"
            )
    )
    public List<UserEntity> userEntityList;

}
