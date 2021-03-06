package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.RemainderEntity;

import java.util.List;

@Dao
public interface RemainderDao {

    @Insert
    public void insertRemainder(RemainderEntity remainder);

    @Update
    public void updateRemainder(RemainderEntity remainder);

    @Delete
    public void deleteRemainder(RemainderEntity remainder);

    @Transaction
    @Query("SELECT * FROM tbl_remainder WHERE habit_id = :habitId")
    public List<RemainderEntity> getRemainderListByHabitId (Long habitId);

    @Transaction
    @Query("DELETE FROM tbl_remainder WHERE habit_id = :habitId")
    public void deleteAllRemainderByHabitId(Long habitId);

    @Query("SELECT * FROM tbl_remainder WHERE hour_time = :h AND minutes_time = :m AND habit_id = :id")
    public RemainderEntity checkExistRemainder(Long h, Long m, Long id);

}
