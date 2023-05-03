package com.android.mobile_project.data.remote.persistence.behavior;

import com.android.mobile_project.data.remote.model.api.JwtResponseModel;
import com.android.mobile_project.data.remote.model.api.SignInRequestModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;

import retrofit2.Call;

public interface IRemoteUserDataSource {

    Call<ResponseModel<JwtResponseModel>> signIn();

}
