package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.DayOfWeekEntity;

import java.util.List;

@Dao
public interface DayOfWeekDao {

    @Insert
    void insertDayOfWeek(DayOfWeekEntity dayOfWeek);

    @Update
    void updateDayOfWeek(DayOfWeekEntity dayOfWeek);

    @Delete
    void deleteDayOfWeek(DayOfWeekEntity dayOfWeek);

    @Query("SELECT * FROM tbl_day_of_week WHERE day_of_week_name = :dayOfWeekName")
    DayOfWeekEntity searchDayOfWeekByName(String dayOfWeekName);

    @Query("SELECT * FROM tbl_day_of_week")
    List<DayOfWeekEntity> getDayOfWeekList();

}
