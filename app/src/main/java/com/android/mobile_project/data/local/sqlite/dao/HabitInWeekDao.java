package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.relation.HabitWithDayOfWeek;

import java.util.List;

@Dao
public interface HabitInWeekDao {

    @Insert
    public void insertHabitInWeek(HabitInWeekEntity habitInWeek);

    @Delete
    public void deleteHabitInWeek(HabitInWeekEntity habitInWeek);

    @Update
    public void updateHabitInWeek(HabitInWeekEntity habitInWeek);

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND habit_id = :habitId ")
    public List<HabitInWeekEntity> getDayOfWeekHabitListByUserAndHabitId(Long userId, Long habitId);

}
