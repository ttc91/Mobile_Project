package com.android.mobile_project.data.remote.persistence;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.persistence.BaseDataSource;
import com.android.mobile_project.data.remote.api.HistoryAPI;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.api.HistorySynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.remote.persistence.behavior.IRemoteHistoryDataSource;
import com.android.mobile_project.data.utils.constant.StringConstant;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemoteHistoryDataSource extends BaseDataSource implements IRemoteHistoryDataSource {

    private static final String TAG = RemoteHistoryDataSource.class.getSimpleName();

    private final HistoryAPI historyAPI;

    private final HistoryDAO dao;

    @Inject
    public RemoteHistoryDataSource(HistoryAPI historyAPI, HistoryDAO dao) {
        this.historyAPI = historyAPI;
        this.dao = dao;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Call<ResponseModel<HistoryModel>> synchronize() {
        String username = DataLocalManager.getInstance().getUserName();
        List<HistoryModel> historyModels = HistoryMapper.getInstance().mapToListModel(dao.getAll());
        HistorySynchronizeModel synchronizeModel = new HistorySynchronizeModel();
        synchronizeModel.setUsername(username);
        synchronizeModel.setDataList(historyModels);
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        return historyAPI.synchronize(token, synchronizeModel);
    }

    @Override
    public Single<ResponseModel<HistoryModel>> getAllHistory() {
        String username = DataLocalManager.getInstance().getUserName();
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        return historyAPI.getAllHistory(token, username);
    }
}
