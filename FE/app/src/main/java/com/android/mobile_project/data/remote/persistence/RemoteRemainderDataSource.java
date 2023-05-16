package com.android.mobile_project.data.remote.persistence;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.RemainderDAO;
import com.android.mobile_project.data.local.sqlite.persistence.BaseDataSource;
import com.android.mobile_project.data.remote.api.RemainderAPI;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.remote.model.api.RemainderSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.remote.persistence.behavior.IRemoteRemainderDataSource;
import com.android.mobile_project.data.utils.constant.StringConstant;
import com.android.mobile_project.data.utils.mapper.RemainderMapper;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemoteRemainderDataSource extends BaseDataSource implements IRemoteRemainderDataSource {

    private final RemainderAPI remainderAPI;

    private final RemainderDAO dao;

    @Inject
    public RemoteRemainderDataSource(RemainderAPI remainderAPI, RemainderDAO dao) {
        this.remainderAPI = remainderAPI;
        this.dao = dao;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Call<ResponseModel<RemainderModel>> synchronize() {
        String username = DataLocalManager.getInstance().getUserName();
        List<RemainderModel> remainderModels = RemainderMapper.getInstance().mapToListModel(dao.getAll());
        RemainderSynchronizeModel synchronizeModel = new RemainderSynchronizeModel();
        synchronizeModel.setUsername(username);
        synchronizeModel.setDataList(remainderModels);
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        return remainderAPI.synchronize(token, synchronizeModel);
    }

    @Override
    public Single<ResponseModel<RemainderModel>> getAllReminder() {
        String username = DataLocalManager.getInstance().getUserName();
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        return remainderAPI.getAllReminder(token, username);
    }

}
