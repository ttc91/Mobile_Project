package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface HistoryDAO extends BaseDAO<HistoryEntity>{

    @Query("SELECT * FROM tbl_history")
    Flowable<List<HistoryEntity>> getAllHistoryList();

    @Query("SELECT * FROM tbl_history WHERE habit_id = :habitId AND user_id = :userId")
    Flowable<List<HistoryEntity>> getHistoryListByAndUserIdHabitId(Long habitId, Long userId);

    @Query("SELECT * FROM tbl_history WHERE habit_id = :hId AND date = :date")
    Single<HistoryEntity> getHistoryByHabitIdAndDate(Long hId, String date);

    @Query("SELECT * FROM tbl_history WHERE user_id = :uId AND date = :date")
    Flowable<List<HistoryEntity>> getHistoryByDate(Long uId, String date);

    @Query("SELECT * FROM tbl_history WHERE user_id = :uId AND date = :date")
    Single<List<HistoryEntity>> getHistoryByDateSingle(Long uId, String date);

    @Transaction
    @Query("UPDATE tbl_history SET state = 'true' WHERE user_id = :userId AND habit_id = :habitId AND date = :date")
    Completable updateHistoryStatusTrueWithUserIdAndHabitIdAndDate(Long userId, Long habitId, String date);

    @Query("SELECT COUNT(habit_id) FROM tbl_history WHERE state = 'true' AND date = :historyDate")
    Single<Long> countTrueStateByHistoryDate(String historyDate);

    @Query("SELECT COUNT(habit_id) FROM tbl_history WHERE date = :historyDate")
    Single<Long> countHistoriesByDate(String historyDate);

    /**
     * Query do in background
     */

    @Insert
    void insertInBackground(HistoryEntity entity);

    @Query("SELECT * FROM tbl_history WHERE habit_id = :hId AND date = :date")
    HistoryEntity getHistoryByHabitIdAndDateInBackground(Long hId, String date);

}
