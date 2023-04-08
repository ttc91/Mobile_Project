package com.android.mobile_project.ui.activity.base;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.utils.custom.SingleLiveEvent;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public abstract class BaseViewModel extends ViewModel {

    protected CompositeDisposable mMainCompDisposable = new CompositeDisposable();
    protected CompositeDisposable mFlowableCompDisposable = new CompositeDisposable();
    protected SingleLiveEvent<Boolean> mLiveDataIsLoading = new SingleLiveEvent<>();
    protected SingleLiveEvent<Boolean> mLiveDataIsSuccess = new SingleLiveEvent<>();
    protected SingleLiveEvent<Throwable> mLiveDataOnError = new SingleLiveEvent<>();
    public LiveData<Boolean> getLiveDataIsLoading() {
        return mLiveDataIsLoading;
    }
    public LiveData<Boolean> getLiveDataIsSuccess() {
        return mLiveDataIsSuccess;
    }
    public LiveData<Throwable> getLiveDataIsError() { return mLiveDataOnError; }

    public LiveData<Throwable> getLiveDataOnError() {
        return mLiveDataOnError;
    }

    public abstract class CustomSingleObserver<T> implements SingleObserver<T> {
        @Override
        public void onSubscribe(Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(Throwable e) {
            mLiveDataOnError.postValue(e);
            e.printStackTrace();
        }
    }

    public abstract class CustomCompletableObserver implements CompletableObserver {
        @Override
        public void onSubscribe(Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(Throwable e) {
            mLiveDataOnError.postValue(e);
            e.printStackTrace();
        }
    }

    public abstract class CustomObserver<T> implements Observer<T> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            mLiveDataOnError.postValue(e);
        }

        @Override
        public void onComplete() {
            mLiveDataIsSuccess.postValue(true);
        }

    }

    public abstract class CustomSubscriber<T> extends DisposableSubscriber<T> {
        public CustomSubscriber() {
            super();
        }

        @Override
        protected void onStart() {
            super.onStart();
        }


        @Override
        public void onComplete() {

        }


        @Override
        public void onError(Throwable e) {
        }

    }

    protected void addDisposable(Disposable disposable) {
        if (mFlowableCompDisposable == null) {
            mFlowableCompDisposable = new CompositeDisposable();
        }
        mFlowableCompDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mMainCompDisposable != null && mMainCompDisposable.isDisposed()) {
            mMainCompDisposable.dispose();
            mMainCompDisposable.clear();
        }
        if (mFlowableCompDisposable != null && mFlowableCompDisposable.isDisposed()) {
            mFlowableCompDisposable.dispose();
            mFlowableCompDisposable.clear();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        unDisposable();
    }


}
