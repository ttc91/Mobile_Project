package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
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

@Dao
public interface HabitDao {

    @Insert
    public void insertHabit(HabitEntity habit);

    @Delete
    public void deleteHabit(HabitEntity habit);

    @Update
    public void updateHabit(HabitEntity habit);

    @Query("DELETE FROM tbl_habit WHERE id = :id")
    public void deleteHabitById(Long id);

    @Transaction
    @Query("SELECT * FROM tbl_user")
    public List<UserWithHabit> getHabitListByUser();

    @Transaction
    @Query("SELECT * FROM tbl_day_of_time")
    public List<DayOfTimeWithHabit> getHabitListByDayOfTime();

    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId AND day_of_time_id = :dayOfTimeId")
    public List<HabitEntity> getHabitListByUserAndDayOfTime(Long userId, Long dayOfTimeId);

    @Query("SELECT * FROM tbl_habit WHERE habit_name = :name")
    public HabitEntity getHabitByName(String name);

    @Query("SELECT * FROM tbl_habit WHERE user_id = :id")
    public List<HabitEntity> getHabitListByUserId(Long id);

    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId AND id = :habitId")
    public HabitEntity getHabitByUserIdAndHabitId(Long userId, Long habitId);

    @Query("UPDATE tbl_habit SET day_of_time_id = :dayOfTimeId WHERE id = :habitId")
    public void updateDateOfTimeInHabit(Long dayOfTimeId, Long habitId);

    @Query("UPDATE tbl_habit SET habit_name = :hName WHERE id = :habitId")
    public void updateNameOfHabit(String hName, Long habitId);

    @Query("SELECT * FROM tbl_habit ORDER BY longest_steak DESC")
    public List<HabitEntity> getHabitListDescByLongestSteak();

}
