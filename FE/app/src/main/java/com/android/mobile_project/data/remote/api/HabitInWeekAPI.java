package com.android.mobile_project.data.remote.api;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.api.HabitInWeekSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.utils.constant.ApiPaths;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HabitInWeekAPI {

    @POST(ApiPaths.API_HABIT_IN_WEEK_DOMAIN + ApiPaths.API_SYNCHRONIZE_DOMAIN)
    Call<ResponseModel<HabitInWeekModel>> synchronize (@Header("Authorization") String token, @Body HabitInWeekSynchronizeModel model);

}
