package com.android.mobile_project.data.remote.persistence;

import android.util.Log;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.UserDAO;
import com.android.mobile_project.data.remote.api.UserAPI;
import com.android.mobile_project.data.remote.model.api.JwtResponseModel;
import com.android.mobile_project.data.remote.model.api.SignInRequestModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.remote.persistence.behavior.IRemoteUserDataSource;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import retrofit2.Call;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemoteUserDataSource implements IRemoteUserDataSource {

    private final UserAPI userAPI;

    private final UserDAO dao;

    @Inject
    public RemoteUserDataSource(UserAPI userAPI, UserDAO dao) {
        this.userAPI = userAPI;
        this.dao = dao;
    }

    @Override
    public Call<ResponseModel<JwtResponseModel>> signIn() {
        Log.d("RemoteUserDataSource", "signIn: ");
        String username = DataLocalManager.getInstance().getUserName();
        SignInRequestModel model = new SignInRequestModel(username);
        return userAPI.signIn(model);
    }

}
