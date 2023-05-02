package com.android.mobile_project.ui.activity.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.remote.model.UserModel;
import com.android.mobile_project.data.remote.model.api.JwtResponseModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.repository.UserRepository;
import com.android.mobile_project.data.utils.mapper.UserMapper;
import com.android.mobile_project.ui.activity.login.service.DbService;
import com.android.mobile_project.ui.activity.login.service.InitService;
import com.android.mobile_project.ui.activity.login.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@MyCustomAnnotation.MyScope.ActivityScope
public class LoginViewModel extends ViewModel {

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
    public LoginViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
    }

    protected ToastService toastService;

    protected DbService dbService;

    protected InitService initService;

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

    protected void requestSignInToServer(){
        mUserRepository.getMRemoteUserDataSource().signIn().enqueue(new Callback<ResponseModel<JwtResponseModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Response<ResponseModel<JwtResponseModel>> response) {
                Log.i("LoginViewModel - signIn", "onComplete");
                DataLocalManager.getInstance().setToken(response.body().getObject().getToken());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Throwable t) {
                Log.e("LoginViewModel - signIn", "onFailure");
            }
        });
    }

}
