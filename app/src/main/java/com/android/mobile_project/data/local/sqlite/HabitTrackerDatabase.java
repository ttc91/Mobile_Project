package com.android.mobile_project.data.local.sqlite;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
        HabitInWeekEntity.class, HistoryEntity.class, RemainderEntity.class}, version = 5)
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
                    .addCallback(roomCallBack)
                    .build();

        }
        return instance;

    }

    public static RoomDatabase.Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulatedDbAsyncTask(instance).execute();
        }
    };

    public static class PopulatedDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private DayOfWeekDao dayOfWeekDao;
        private DayOfTimeDao dayOfTimeDao;

        private PopulatedDbAsyncTask(HabitTrackerDatabase db){
            this.dayOfTimeDao = db.dayOfTimeDao();
            this.dayOfWeekDao = db.dayOfWeekDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DayOfTimeEntity afternoonEntity = new DayOfTimeEntity();
            DayOfTimeEntity morningEntity = new DayOfTimeEntity();
            DayOfTimeEntity nightEntity = new DayOfTimeEntity();
            DayOfTimeEntity anyTimeEntity = new DayOfTimeEntity();

            afternoonEntity.dayOfTimeName = "Afternoon";
            morningEntity.dayOfTimeName = "Morning";
            nightEntity.dayOfTimeName = "Night";
            anyTimeEntity.dayOfTimeName = "Anytime";

            this.dayOfTimeDao.insertDayOfTime(afternoonEntity);
            this.dayOfTimeDao.insertDayOfTime(morningEntity);
            this.dayOfTimeDao.insertDayOfTime(nightEntity);
            this.dayOfTimeDao.insertDayOfTime(anyTimeEntity);

            DayOfWeekEntity sunday = new DayOfWeekEntity();
            DayOfWeekEntity monday = new DayOfWeekEntity();
            DayOfWeekEntity tuesday = new DayOfWeekEntity();
            DayOfWeekEntity wednesday = new DayOfWeekEntity();
            DayOfWeekEntity thursday = new DayOfWeekEntity();
            DayOfWeekEntity friday = new DayOfWeekEntity();
            DayOfWeekEntity saturday = new DayOfWeekEntity();

            sunday.dayOfWeekName = "Sunday";
            monday.dayOfWeekName = "Monday";
            tuesday.dayOfWeekName = "Tuesday";
            wednesday.dayOfWeekName = "Wednesday";
            thursday.dayOfWeekName = "Thursday";
            friday.dayOfWeekName = "Friday";
            saturday.dayOfWeekName = "Saturday";

            this.dayOfWeekDao.insertDayOfWeek(sunday);
            this.dayOfWeekDao.insertDayOfWeek(monday);
            this.dayOfWeekDao.insertDayOfWeek(tuesday);
            this.dayOfWeekDao.insertDayOfWeek(wednesday);
            this.dayOfWeekDao.insertDayOfWeek(thursday);
            this.dayOfWeekDao.insertDayOfWeek(friday);
            this.dayOfWeekDao.insertDayOfWeek(saturday);

            Log.e("INSERT", "OK !");

            return null;
        }
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
