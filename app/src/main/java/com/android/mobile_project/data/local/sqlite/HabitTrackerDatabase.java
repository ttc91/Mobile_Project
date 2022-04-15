package com.android.mobile_project.data.local.sqlite;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.mobile_project.data.local.model.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.model.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.model.db.RemainderEntity;
import com.android.mobile_project.data.local.model.db.UserEntity;
import com.android.mobile_project.data.local.sqlite.dao.DayOfTimeDao;
import com.android.mobile_project.data.local.sqlite.dao.DayOfWeekDao;
import com.android.mobile_project.data.local.sqlite.dao.HabitDao;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDao;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDao;
import com.android.mobile_project.data.local.sqlite.dao.RemainderDao;
import com.android.mobile_project.data.local.sqlite.dao.UserDao;

@androidx.room.Database(entities = {UserEntity.class, HabitEntity.class, DayOfWeekEntity.class, DayOfTimeEntity.class,
        HabitInWeekEntity.class, HistoryEntity.class, RemainderEntity.class}, version = 1)
public abstract class HabitTrackerDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "habit_tracker.db";
    private static HabitTrackerDatabase instance;

    /**
     * Construction :
     */

    public static synchronized HabitTrackerDatabase getInstance(Context context){

        if(instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(), HabitTrackerDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;

    }

    /**
     * Data Access Object
     */

    public abstract UserDao userDao();
    public abstract HabitDao habitDao();
    public abstract DayOfTimeDao dayOfTimeDao();
    public abstract DayOfWeekDao dayOfWeekDao();
    public abstract HistoryDao historyDao();
    public abstract HabitInWeekDao habitInWeekDao();
    public abstract RemainderDao remainderDao();

}
