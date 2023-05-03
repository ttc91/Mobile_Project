package com.android.mobile_project.data.local.sqlite.entity.db.relation;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;

public class UserWithHistory {

    @Embedded public UserEntity userEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public HistoryEntity historyEntity;
}
