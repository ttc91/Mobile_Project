package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.UserEntity;
import com.android.mobile_project.data.local.model.db.relation.DayOfTimeWithHabit;
import com.android.mobile_project.data.local.model.db.relation.UserWithHabit;

import java.util.List;

public interface HabitDao {

    @Insert
    public void insertHabit(HabitEntity habit);

    @Delete
    public void deleteHabit(Long habitId);

    @Update
    public void updateHabit(HabitEntity habit);

    @Transaction
    @Query("SELECT * FROM tbl_user")
    public List<UserWithHabit> getHabitListByUser();

    @Transaction
    @Query("SELECT * FROM tbl_day_of_time")
    public List<DayOfTimeWithHabit> getHabitListByDayOfTime();

    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId AND day_of_time_id = :dayOfTimeId")
    public List<HabitEntity> getHabitListByUserAndDayOfTime(Long userId, Long dayOfTimeId);

}
