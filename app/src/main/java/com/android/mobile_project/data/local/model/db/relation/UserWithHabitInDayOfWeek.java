package com.android.mobile_project.data.local.model.db.relation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.UserEntity;

import java.util.List;

public class UserWithHabitInDayOfWeek {

    @Embedded public UserEntity userEntity;

    @Relation(

            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(HabitInWeekEntity.class)

    )

    public List<HabitEntity> habitEntityList;
}
