package com.android.mobile_project.data.local.sqlite.entity.db.relation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;

import java.util.List;

public class UserWithHabitForHabitInWeek {

    @Embedded public UserEntity userEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = HabitInWeekEntity.class,
                    parentColumn = "user_id",
                    entityColumn = "habit_id"
            )
    )
    public List<HabitEntity> habitEntityList;
}
