package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface HabitInWeekDAO extends BaseDAO<HabitInWeekEntity>{

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND habit_id = :habitId ")
    Flowable<List<HabitInWeekEntity>> getDayOfWeekHabitListByUserAndHabitId(Long userId, Long habitId);

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND day_of_week_id = :id")
    Flowable<List<HabitInWeekEntity>> getHabitInWeekEntityByDayOfWeekId(Long userId, Long id);

    @Transaction
    @Query("DELETE FROM tbl_habit_in_week WHERE habit_id = :habitId")
    Completable deleteHabitInWeekByHabitId(Long habitId);

    /**
     * Query do in background
     */

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND day_of_week_id = :id")
    List<HabitInWeekEntity> getHabitInWeekEntityByDayOfWeekIdInBackground(Long userId, Long id);

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND habit_id = :habitId AND day_of_week_id = :id")
    HabitInWeekEntity getDayOfWeekHabitListByUserAndHabitIdAndId(Long userId, Long habitId, Long id);

    @Query("SELECT * FROM tbl_habit_in_week")
    List<HabitInWeekEntity> getAll();

}
