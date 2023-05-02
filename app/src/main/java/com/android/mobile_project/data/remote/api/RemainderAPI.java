package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.remote.model.api.RemainderSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RemainderAPI {

    @POST(ApiPaths.API_REMAINDER_DOMAIN + ApiPaths.API_SYNCHRONIZE_DOMAIN)
    Call<ResponseModel<RemainderModel>> synchronize (@Header("Authorization") String token, @Body RemainderSynchronizeModel model);

}
