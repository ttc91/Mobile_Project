package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;

import java.util.List;

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

}
