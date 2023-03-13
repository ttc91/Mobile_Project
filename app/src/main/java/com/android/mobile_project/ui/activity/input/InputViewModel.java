package com.android.mobile_project.ui.activity.input;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.remote.model.UserModel;
import com.android.mobile_project.data.repository.UserRepository;
import com.android.mobile_project.data.utils.mapper.UserMapper;
import com.android.mobile_project.ui.activity.input.service.DbService;
import com.android.mobile_project.ui.activity.input.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@MyCustomAnnotation.MyScope.ActivityScope
public class InputViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    @Inject
    public InputViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
    }

    protected DbService service;
    protected ToastService toastService;

    protected void insertUser(UserModel userModel) {
        mUserRepository.getMUserDataSource().insert(UserMapper.getInstance().mapToEntity(userModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("insertUser", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertUser", "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertUser", "onError", e);
                    }
                });
    }

    protected Long getUserIdByName(String username) {

        final Long[] userIds = new Long[]{0L};

        mUserRepository.getMUserDataSource().getUserIdByName(username)
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("getUserIdByName", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        Log.i("getUserIdByName", "onSuccess");
                        userIds[0] = aLong;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("getUserIdByName", "onError", e);
                    }
                });
        return userIds[0];
    }

}
