package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RemainderDAO extends BaseDAO<RemainderEntity>{

    @Transaction
    @Query("SELECT * FROM tbl_remainder WHERE habit_id = :habitId")
    Flowable<List<RemainderEntity>> getRemainderListByHabitId (Long habitId);

    @Transaction
    @Query("DELETE FROM tbl_remainder WHERE habit_id = :habitId")
    Completable deleteAllRemainderByHabitId(Long habitId);

    @Query("SELECT * FROM tbl_remainder WHERE hour_time = :h AND minutes_time = :m AND habit_id = :id")
    Single<RemainderEntity> checkExistRemainder(Long h, Long m, Long id);

    @Transaction
    @Query("DELETE FROM tbl_remainder WHERE hour_time = :h AND minutes_time = :m AND habit_id = :id")
    Completable deleteRemainderByTimerHourAndTimerMinutesAndId(Long h, Long m, Long id);

    @Query("SELECT * FROM tbl_remainder")
    List<RemainderEntity> getAll();

    @Insert
    Completable insertAll(RemainderEntity... habitEntities);

    @Query("DELETE FROM tbl_remainder")
    Completable deleteAll();

}
