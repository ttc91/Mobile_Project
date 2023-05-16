package com.android.mobile_project.data.remote.persistence.behavior;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;


import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface IRemoteHabitDataSource {

    Call<ResponseModel<HabitModel>> synchronize();
    Single<ResponseModel<HabitModel>> getAllHabit();

}
