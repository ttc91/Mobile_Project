package com.android.mobile_project.data.local.model.db.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.UserEntity;

import java.util.List;

public class UserWithHabit {

    @Embedded public UserEntity userEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )

    public List<HabitEntity> habitEntityList;

}
