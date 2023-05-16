package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.api.HabitSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HabitAPI {

    @POST(ApiPaths.API_HABIT_DOMAIN + ApiPaths.API_SYNCHRONIZE_DOMAIN)
    Call<ResponseModel<HabitModel>> synchronize(@Header("Authorization") String token, @Body HabitSynchronizeModel model);

    @GET("habit/get_all/{username}")
    @Headers("Content-type: application/json")
    Single<ResponseModel<HabitModel>> getAllHabit(@Header("Authorization") String token, @Path("username") String username);

}
