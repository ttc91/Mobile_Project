package com.android.mobile_project.ui.activity.input;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.remote.model.UserModel;
import com.android.mobile_project.data.repository.UserRepository;
import com.android.mobile_project.data.utils.mapper.UserMapper;
import com.android.mobile_project.ui.activity.input.service.DbService;
import com.android.mobile_project.ui.activity.input.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@MyCustomAnnotation.MyScope.ActivityScope
public class InputViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    private final MutableLiveData<Long> mUserIdMutableLiveData = new MutableLiveData<>();

    protected LiveData<Long> getUserIdMutableLiveData() {
        return mUserIdMutableLiveData;
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void setDispose(){
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }

    @Inject
    public InputViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
    }

    protected ToastService toastService;

    protected DbService service;

    protected void insertUser(UserModel userModel, DbService.InsertUserResult callback){
        mCompositeDisposable.add(
                mUserRepository.getMUserDataSource().insert(UserMapper.getInstance().mapToEntity(userModel))
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    Log.i("insertUser", "onComplete");
                    callback.onInsertUserSuccess();
                }, throwable -> {
                    Log.e("insertUser", "onError", throwable);
                    callback.onInsertUserFailure();
                })
        );
    }

    protected void getUserIdByName(String username, DbService.GetUserIdFromLocalResult callback) {

        mCompositeDisposable.add(
                mUserRepository.getMUserDataSource().getUserIdByName(username)
                .observeOn(Schedulers.single())
                .subscribeOn(Schedulers.io())
                .subscribe(aLong -> {
                    Log.i("getUserIdByName", "onSuccess");
                    mUserIdMutableLiveData.postValue(aLong);
                    callback.onGetIdSuccess();
                }, throwable -> {
                    Log.e("getUserIdByName", "onError", throwable);
                    callback.onGetIdFailure();
                })
        );

    }

}
