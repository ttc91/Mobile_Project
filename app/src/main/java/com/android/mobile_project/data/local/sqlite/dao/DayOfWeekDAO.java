package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.DayOfWeekWithHabitForHabitInWeek;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.DayOfWeekWithUserForHabitInWeek;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DayOfWeekDAO extends BaseDAO<DayOfWeekEntity>{

    /**
     * <b>Many-to-many</b> Day of week with <b>User</b> but in table HabitInWeek
     */
    @Transaction
    @Query("SELECT * FROM tbl_day_of_week")
    List<DayOfWeekWithUserForHabitInWeek> getDayOfWeekWithUserForHabitInWeeks();

    /**
     * <b>Many-to-many</b> Day of week with <b>Habit</b> but in table HabitInWeek
     */
    @Transaction
    @Query("SELECT * FROM tbl_day_of_week")
    List<DayOfWeekWithHabitForHabitInWeek> getDayOfWeekWithHabitForHabitInWeeks();

    @Query("SELECT * FROM tbl_day_of_week WHERE day_of_week_name = :dayOfWeekName")
    Single<DayOfWeekEntity> searchDayOfWeekByName(String dayOfWeekName);

    @Query("SELECT * FROM tbl_day_of_week")
    Flowable<List<DayOfWeekEntity>> getDayOfWeekList();

    @Query("SELECT * FROM tbl_day_of_week WHERE id = :id")
    Single<DayOfWeekEntity> getDayOfWeekById(Long id);

}
