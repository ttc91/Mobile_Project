package com.android.mobile_project.data.remote.persistence.behavior;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;

import retrofit2.Call;

public interface IRemoteHabitInWeekDataSource {

    Call<ResponseModel<HabitInWeekModel>> synchronize ();

}
