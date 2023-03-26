package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.DayOfTimeWithHabit;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DayOfTimeDAO extends BaseDAO<DayOfTimeEntity>{

    /**
     * <b>One-to-one</b> Habit with day of time.
     */
    @Transaction
    @Query("SELECT * FROM tbl_day_of_time")
    Flowable<List<DayOfTimeWithHabit>> getDayOfTimeWithHabits();

    @Query("SELECT * FROM tbl_day_of_time WHERE time_name = :dayOfTimeName")
    Single<DayOfTimeEntity> searchDayOfTimeByName(String dayOfTimeName);

    @Query("SELECT * FROM tbl_day_of_time")
    Flowable<List<DayOfTimeEntity>> getDayOfTimeList();

    @Query("SELECT * FROM tbl_day_of_time WHERE id = :id")
    Single<DayOfTimeEntity> getDayOfTimeById(Long id);

    @Insert
    void insertInBackgroundDb(DayOfTimeEntity e);

}
