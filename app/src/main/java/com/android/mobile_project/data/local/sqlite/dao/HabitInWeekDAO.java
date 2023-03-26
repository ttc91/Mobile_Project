package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface HabitInWeekDAO extends BaseDAO<HabitInWeekEntity>{

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND habit_id = :habitId ")
    Flowable<List<HabitInWeekEntity>> getDayOfWeekHabitListByUserAndHabitId(Long userId, Long habitId);

    @Query("SELECT * FROM tbl_habit_in_week WHERE user_id = :userId AND day_of_week_id = :id")
    Flowable<List<HabitInWeekEntity>> getHabitInWeekEntityByDayOfWeekId(Long userId, Long id);

}
