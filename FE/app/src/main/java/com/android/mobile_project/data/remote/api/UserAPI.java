package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.api.JwtResponseModel;
import com.android.mobile_project.data.remote.model.api.SignInRequestModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {

    @POST(ApiPaths.API_USER_DOMAIN + ApiPaths.API_SIGN_IN_DOMAIN)
    Call<ResponseModel<JwtResponseModel>> signIn(@Body SignInRequestModel model);

}
