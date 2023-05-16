package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.api.HabitInWeekSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HabitInWeekAPI {

    @POST(ApiPaths.API_HABIT_IN_WEEK_DOMAIN + ApiPaths.API_SYNCHRONIZE_DOMAIN)
    Call<ResponseModel<HabitInWeekModel>> synchronize (@Header("Authorization") String token, @Body HabitInWeekSynchronizeModel model);

    @GET(ApiPaths.API_HABIT_IN_WEEK_DOMAIN + ApiPaths.API_GET_ALL_COLLECTION_DOMAIN + "/{username}")
    Single<ResponseModel<HabitInWeekModel>> getAllHabitInWeek(@Header("Authorization") String token, @Path("username") String username);

}
