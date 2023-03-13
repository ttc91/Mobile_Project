package com.android.mobile_project.data.local.sqlite.persistence.behavior;

import io.reactivex.rxjava3.core.Completable;

public interface BaseDataSource<T> {

    Completable insert(T t);

    Completable delete(T t);

    Completable update(T t);

}
