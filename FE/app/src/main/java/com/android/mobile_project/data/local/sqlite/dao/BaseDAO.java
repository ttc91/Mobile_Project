package com.android.mobile_project.data.local.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface BaseDAO<T> {

    @Insert
    Completable insert(T t);

    @Delete
    Completable delete(T t);

    @Update
    Completable update(T t);

}
