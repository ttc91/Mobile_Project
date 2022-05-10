package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.model.db.relation.HabitWithHistory;
import com.android.mobile_project.data.local.model.db.relation.UserWithHistory;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    public void insertHistory(HistoryEntity history);

    @Update
    public void updateHistory(HistoryEntity historyEntity);

    @Query("SELECT * FROM tbl_history")
    public List<HistoryEntity> getAllHistoryList();

    @Query("SELECT * FROM tbl_history WHERE habit_id = :habitId AND user_id = :userId")
    public List<HistoryEntity> getHistoryListByAndUserIdHabitId(Long habitId, Long userId);

    @Transaction
    @Query("SELECT * FROM tbl_user")
    public List<UserWithHistory> getHistoryByUser();

    @Transaction
    @Query("SELECT * FROM tbl_habit")
    public List<HabitWithHistory> getHistoryByHabit();

    @Query("SELECT * FROM tbl_history WHERE habit_id = :h_id AND date = :date")
    public HistoryEntity getHistoryByHabitIdAndDate(Long h_id, String date);

}
