package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.DayOfTimeEntity;

@Dao
public interface DayOfTimeDao {

    @Insert
    void insertDayOfTime (DayOfTimeEntity dayOfTime);

    @Update
    void updateDayOfTime (DayOfTimeEntity dayOfTime);

    @Delete
    void deleteDayOfTime (DayOfTimeEntity dayOfTime);

    @Query("SELECT * FROM tbl_day_of_time WHERE time_name = :dayOfTimeName")
    DayOfTimeEntity searchDayOfTimeByName(String dayOfTimeName);

}
