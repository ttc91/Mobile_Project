package com.android.mobile_project.data.remote.persistence;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.remote.api.HabitInWeekAPI;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.api.HabitInWeekSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.remote.persistence.behavior.IRemoteHabitInWeekDataSource;
import com.android.mobile_project.data.utils.constant.StringConstant;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemoteHabitInWeekDataSource implements IRemoteHabitInWeekDataSource {

    private final HabitInWeekAPI habitInWeekAPI;

    private final HabitInWeekDAO dao;

    @Inject
    public RemoteHabitInWeekDataSource(HabitInWeekAPI habitInWeekAPI, HabitInWeekDAO dao) {
        this.habitInWeekAPI = habitInWeekAPI;
        this.dao = dao;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Call<ResponseModel<HabitInWeekModel>> synchronize() {
        String username = DataLocalManager.getInstance().getUserName();
        List<HabitInWeekModel> habitInWeekModels = HabitInWeekMapper.getInstance().mapToListModel(dao.getAll());
        HabitInWeekSynchronizeModel synchronizeModel = new HabitInWeekSynchronizeModel();
        synchronizeModel.setUsername(username);
        synchronizeModel.setDataList(habitInWeekModels);
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        return habitInWeekAPI.synchronize(token, synchronizeModel);
    }

}
