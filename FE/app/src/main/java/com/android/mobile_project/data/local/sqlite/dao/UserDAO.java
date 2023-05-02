package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.UserWithDayInWeekForHabitInWeek;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.UserWithHabit;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.UserWithHabitForHabitInWeek;
import com.android.mobile_project.data.local.sqlite.entity.db.relation.UserWithHistory;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDAO extends BaseDAO<UserEntity>{

    /**
     * <b>One-to-many</b> User with habit.
     */
    @Transaction
    @Query("SELECT * FROM tbl_user")
    List<UserWithHabit> getUserWithHabits();

    /**
     * <b>One-to-many</b> User with history.
     */
    @Transaction
    @Query("SELECT * FROM tbl_user")
    List<UserWithHistory> getHistoryByUser();

    /**
     * <b>Many-to-many</b> User with <b>habit</b> but in table HabitInWeek
     */
    @Transaction
    @Query("SELECT * FROM tbl_user")
    List<UserWithHabitForHabitInWeek> getUserWithHabitForHabitInWeeks();

    /**
     * <b>Many-to-many</b> User with <b>Day in week</b> but in table HabitInWeek
     */
    @Transaction
    @Query("SELECT * FROM tbl_user")
    Flowable<List<UserWithDayInWeekForHabitInWeek>> getUserWithDayInWeekForHabitInWeeks();

    @Query("SELECT id FROM tbl_user WHERE user_name = :name")
    Single<Long> getUserIdByName(String name);

}
