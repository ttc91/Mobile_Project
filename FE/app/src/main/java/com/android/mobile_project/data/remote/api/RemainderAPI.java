package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.remote.model.api.RemainderSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RemainderAPI {

    @POST(ApiPaths.API_REMAINDER_DOMAIN + ApiPaths.API_SYNCHRONIZE_DOMAIN)
    Call<ResponseModel<RemainderModel>> synchronize (@Header("Authorization") String token, @Body RemainderSynchronizeModel model);

    @GET(ApiPaths.API_REMAINDER_DOMAIN + ApiPaths.API_GET_ALL_COLLECTION_DOMAIN + "/{username}")
    Single<ResponseModel<RemainderModel>> getAllReminder(@Header("Authorization") String token, @Path("username") String username);

}
