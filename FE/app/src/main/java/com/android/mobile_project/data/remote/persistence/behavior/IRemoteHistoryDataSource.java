package com.android.mobile_project.data.remote.persistence.behavior;

import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;

import retrofit2.Call;

public interface IRemoteHistoryDataSource {

    Call<ResponseModel<HistoryModel>> synchronize ();

}
