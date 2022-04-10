package com.android.mobile_project.data.local.model.db.relation;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.model.db.UserEntity;

public class UserWithHistory {

    @Embedded public UserEntity userEntity;

    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )

    public HistoryEntity historyEntity;
}
