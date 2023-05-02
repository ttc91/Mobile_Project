package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.HabitWithDayOfWeekForHabitInWeek;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.HabitWithHistory;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.HabitWithRemainder;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.HabitWithUserForHabitInWeek;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface HabitDAO extends BaseDAO<HabitEntity>{

    /**
     * <b>Many-to-many</b> Habit with <b>User</b> but in table HabitInWeek
     */
    @Transaction
    @Query("SELECT * FROM tbl_habit")
    List<HabitWithUserForHabitInWeek> getHabitWithUserForHabitInWeeks();

    /**
     * <b>Many-to-many</b> Habit with <b>Day of week</b> but in table HabitInWeek
     */
    @Transaction
    @Query("SELECT * FROM tbl_habit")
    List<HabitWithDayOfWeekForHabitInWeek> getHabitWithDayOfWeekForHabitInWeeks();

    /**
     * <b>One-to-many</b> Habit with history.
     */
    @Transaction
    @Query("SELECT * FROM tbl_habit")
    List<HabitWithHistory> getHabitWithHistories();

    /**
     * <b>One-to-many</b> Habit with remainder.
     */
    @Transaction
    @Query("SELECT * FROM tbl_habit")
    List<HabitWithRemainder> getHabitWithRemainders();

    @Query("DELETE FROM tbl_habit WHERE id = :id")
    Completable deleteHabitById(Long id);

    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId AND day_of_time_id = :dayOfTimeId")
    Flowable<List<HabitEntity>> getHabitListByUserAndDayOfTime(Long userId, Long dayOfTimeId);

    @Query("SELECT * FROM tbl_habit WHERE habit_name = :name")
    Single<HabitEntity> getHabitByName(String name);

    @Query("SELECT * FROM tbl_habit WHERE user_id = :id")
    Flowable<List<HabitEntity>> getHabitListByUserId(Long id);

    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId AND id = :habitId")
    Single<HabitEntity> getHabitByUserIdAndHabitId(Long userId, Long habitId);

    @Query("UPDATE tbl_habit SET day_of_time_id = :dayOfTimeId WHERE id = :habitId")
    Completable updateDateOfTimeInHabit(Long dayOfTimeId, Long habitId);

    @Query("UPDATE tbl_habit SET habit_name = :hName WHERE id = :habitId")
    Completable updateNameOfHabit(String hName, Long habitId);

    @Query("SELECT * FROM tbl_habit ORDER BY longest_steak DESC LIMIT 1")
    Single<HabitEntity> getHabitByMostLongestSteak();

    /**
     * Query do in background
     */
    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId AND id = :habitId")
    HabitEntity getHabitByUserIdAndHabitIdInBackground(Long userId, Long habitId);

    @Query("SELECT * FROM tbl_habit WHERE user_id = :userId ORDER BY id DESC LIMIT 1")
    HabitEntity getFinalHabitByUserIdInBackground(Long userId);

    @Update
    void updateHabitInBackground(HabitEntity entity);

    @Query("SELECT * FROM tbl_habit")
    List<HabitEntity> getAll();

}
