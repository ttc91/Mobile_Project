package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.android.mobile_project.data.local.sqlite.entity.db.StepHistoryEntity;

@Dao
public interface StepHistoryDAO extends BaseDAO<StepHistoryEntity>{

    @Query("SELECT * FROM tbl_step_history WHERE step_history_date = :date")
    StepHistoryEntity findOneByDate(String date);

}
