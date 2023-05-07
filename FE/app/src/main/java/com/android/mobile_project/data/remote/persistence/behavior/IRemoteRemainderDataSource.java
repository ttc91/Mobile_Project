package com.android.mobile_project.data.remote.persistence.behavior;

import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface IRemoteRemainderDataSource {

    Call<ResponseModel<RemainderModel>> synchronize ();
    Single<ResponseModel<RemainderModel>> getAllReminder ();

}
