package com.android.mobile_project.data.local.sqlite;

import androidx.room.RoomDatabase;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.UserEntity;
import com.android.mobile_project.data.local.sqlite.dao.DayOfTimeDAO;
import com.android.mobile_project.data.local.sqlite.dao.DayOfWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.dao.RemainderDAO;
import com.android.mobile_project.data.local.sqlite.dao.UserDAO;

@androidx.room.Database(entities = {UserEntity.class, HabitEntity.class, DayOfWeekEntity.class, DayOfTimeEntity.class,
        HabitInWeekEntity.class, HistoryEntity.class, RemainderEntity.class}, version = 15)
public abstract class HabitTrackerDatabase extends RoomDatabase {

    public abstract UserDAO userDao();
    public abstract HabitDAO habitDao();
    public abstract DayOfTimeDAO dayOfTimeDao();
    public abstract DayOfWeekDAO dayOfWeekDao();
    public abstract HistoryDAO historyDao();
    public abstract HabitInWeekDAO habitInWeekDao();
    public abstract RemainderDAO remainderDao();

}
