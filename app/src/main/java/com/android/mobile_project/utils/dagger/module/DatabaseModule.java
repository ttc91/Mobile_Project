package com.android.mobile_project.utils.dagger.module;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.dao.RemainderDAO;
import com.android.mobile_project.data.local.sqlite.dao.UserDAO;
import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.data.local.sqlite.dao.DayOfTimeDAO;
import com.android.mobile_project.data.local.sqlite.dao.DayOfWeekDAO;
import com.android.mobile_project.data.local.sqlite.persistence.LocalDayOfTimeDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.LocalDayOfWeekDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.LocalHabitDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.LocalHabitInWeekDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.LocalHistoryDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.LocalRemainderDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.LocalUserDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.DayOfTimeDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.DayOfWeekDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HabitInWeekDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.HistoryDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.RemainderDataSource;
import com.android.mobile_project.data.local.sqlite.persistence.behavior.UserDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.time.DayOfTime;
import com.android.mobile_project.utils.time.DayOfWeek;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Module
public final class DatabaseModule {

    private static final String DATABASE_NAME = "habit_tracker.db";
    private static HabitTrackerDatabase DATABASE;

    public DatabaseModule(Application application){

        if(DATABASE == null){
            DATABASE = Room.databaseBuilder(application, HabitTrackerDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            new PopulatedDbAsyncTask(DATABASE).execute();
                        }
                    })
                    .build();
        }

    }

    public static class PopulatedDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final DayOfWeekDAO dayOfWeekDao;
        private final DayOfTimeDAO dayOfTimeDao;

        private final CompositeDisposable mCompositeDisposable= new CompositeDisposable();

        private PopulatedDbAsyncTask(HabitTrackerDatabase db){
            this.dayOfWeekDao = db.dayOfWeekDao();
            this.dayOfTimeDao = db.dayOfTimeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DayOfTimeEntity afternoonEntity = new DayOfTimeEntity(DayOfTime.AFTERNOON.getId(), DayOfTime.AFTERNOON.getTimeName());
            DayOfTimeEntity morningEntity = new DayOfTimeEntity(DayOfTime.MORNING.getId(), DayOfTime.MORNING.getTimeName());
            DayOfTimeEntity nightEntity = new DayOfTimeEntity(DayOfTime.NIGHT.getId(), DayOfTime.NIGHT.getTimeName());
            DayOfTimeEntity anyTimeEntity = new DayOfTimeEntity(DayOfTime.ANYTIME.getId(), DayOfTime.ANYTIME.getTimeName());

            mCompositeDisposable.add(this.dayOfTimeDao.insert(afternoonEntity)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfTime", afternoonEntity.dayOfTimeName),
                            throwable -> Log.e("insertDayOfTime", "fail", throwable
                    )
            ));
            mCompositeDisposable.add(this.dayOfTimeDao.insert(morningEntity)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfTime", morningEntity.dayOfTimeName),
                            throwable -> Log.e("insertDayOfTime", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfTimeDao.insert(nightEntity)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfTime", nightEntity.dayOfTimeName),
                            throwable -> Log.e("insertDayOfTime", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfTimeDao.insert(anyTimeEntity)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfTime", anyTimeEntity.dayOfTimeName),
                            throwable -> Log.e("insertDayOfTime", "fail", throwable)
                    ));

            DayOfWeekEntity sunday = new DayOfWeekEntity(DayOfWeek.SUN.getId(), DayOfWeek.SUN.getDayName());
            DayOfWeekEntity monday = new DayOfWeekEntity(DayOfWeek.MON.getId(), DayOfWeek.MON.getDayName());
            DayOfWeekEntity tuesday = new DayOfWeekEntity(DayOfWeek.TUE.getId(), DayOfWeek.TUE.getDayName());
            DayOfWeekEntity wednesday = new DayOfWeekEntity(DayOfWeek.WED.getId(), DayOfWeek.WED.getDayName());
            DayOfWeekEntity thursday = new DayOfWeekEntity(DayOfWeek.THU.getId(), DayOfWeek.THU.getDayName());
            DayOfWeekEntity friday = new DayOfWeekEntity(DayOfWeek.FRI.getId(), DayOfWeek.FRI.getDayName());
            DayOfWeekEntity saturday = new DayOfWeekEntity(DayOfWeek.SAT.getId(), DayOfWeek.SAT.getDayName());

            mCompositeDisposable.add(this.dayOfWeekDao.insert(sunday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", sunday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfWeekDao.insert(monday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", monday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfWeekDao.insert(tuesday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", tuesday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfWeekDao.insert(wednesday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", wednesday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfWeekDao.insert(thursday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", thursday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfWeekDao.insert(friday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", friday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));
            mCompositeDisposable.add(this.dayOfWeekDao.insert(saturday)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> Log.i("insertDayOfWeek", saturday.dayOfWeekName),
                            throwable -> Log.e("insertDayOfWeek", "fail", throwable)
                    ));

            mCompositeDisposable.clear();
            mCompositeDisposable.dispose();

            return null;
        }
    }

    @Provides
    @Singleton
    public UserDAO provideUserDAO(){
        return DATABASE.userDao();
    }

    @Provides
    @Singleton
    public RemainderDAO provideRemainDAO(){
        return DATABASE.remainderDao();
    }

    @Provides
    @Singleton
    public HistoryDAO provideHistoryDAO(){
        return DATABASE.historyDao();
    }

    @Provides
    @Singleton
    public HabitInWeekDAO provideHabitInWeekDAO(){
        return DATABASE.habitInWeekDao();
    }

    @Provides
    @Singleton
    public HabitDAO provideHabitDAO(){
        return DATABASE.habitDao();
    }

    @Provides
    @Singleton
    public DayOfWeekDAO provideDayOfWeekDAO(){
        return DATABASE.dayOfWeekDao();
    }

    @Provides
    @Singleton
    public DayOfTimeDAO provideDayOfTimeDAO(){
        return DATABASE.dayOfTimeDao();
    }

    @Provides
    @Singleton
    public HabitDataSource provideHabitDataSource(HabitDAO dao){
        return new LocalHabitDataSource(dao);
    }

    @Provides
    @Singleton
    public RemainderDataSource provideRemainderDataSource(RemainderDAO dao){
        return new LocalRemainderDataSource(dao);
    }

    @Provides
    @Singleton
    public HabitInWeekDataSource provideHabitInWeekDataSource(HabitInWeekDAO dao){
        return new LocalHabitInWeekDataSource(dao);
    }

    @Provides
    @Singleton
    public DayOfWeekDataSource provideDayOfWeekDataSource(DayOfWeekDAO dao){
        return new LocalDayOfWeekDataSource(dao);
    }

    @Provides
    @Singleton
    public DayOfTimeDataSource provideDayOfTimeDataSource(DayOfTimeDAO dao){
        return new LocalDayOfTimeDataSource(dao);
    }

    @Provides
    @Singleton
    public HistoryDataSource provideHistoryDataSource(HistoryDAO dao){
        return new LocalHistoryDataSource(dao);
    }

    @Provides
    @Singleton
    public UserDataSource provideUserDataSource(UserDAO dao){
        return new LocalUserDataSource(dao);
    }

}
