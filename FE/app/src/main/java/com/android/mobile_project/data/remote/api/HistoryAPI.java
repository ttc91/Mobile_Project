package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.api.HistorySynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoryAPI {

    @POST(ApiPaths.API_HISTORY_DOMAIN + ApiPaths.API_SYNCHRONIZE_DOMAIN)
    Call<ResponseModel<HistoryModel>> synchronize (@Header("Authorization") String token, @Body HistorySynchronizeModel model);

    @GET(ApiPaths.API_HISTORY_DOMAIN + ApiPaths.API_GET_ALL_COLLECTION_DOMAIN + "/{username}")
    Single<ResponseModel<HistoryModel>> getAllHistory(@Header("Authorization") String token, @Path("username") String username);

}
