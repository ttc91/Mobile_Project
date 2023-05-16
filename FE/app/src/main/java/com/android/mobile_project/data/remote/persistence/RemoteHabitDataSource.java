package com.android.mobile_project.data.remote.persistence;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.persistence.BaseDataSource;
import com.android.mobile_project.data.remote.api.HabitAPI;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.api.HabitSynchronizeModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.remote.persistence.behavior.IRemoteHabitDataSource;
import com.android.mobile_project.data.utils.constant.StringConstant;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

@MyCustomAnnotation.MyScope.ActivityScope
public class RemoteHabitDataSource extends BaseDataSource implements IRemoteHabitDataSource {

    private final HabitAPI habitAPI;

    private final HabitDAO dao;

    @Inject
    public RemoteHabitDataSource(HabitAPI habitAPI, HabitDAO dao) {
        this.habitAPI = habitAPI;
        this.dao = dao;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Call<ResponseModel<HabitModel>> synchronize() {
        String username = DataLocalManager.getInstance().getUserName();
        List<HabitModel> habitModels = HabitMapper.getInstance().mapToListModel(dao.getAll());
        for(HabitModel model : habitModels){
            model.setUsername(username);
            model.setDateOfTime(model.getDayOfTimeId());
        }
        HabitSynchronizeModel synchronizeModel = new HabitSynchronizeModel();
        synchronizeModel.setDataList(habitModels);
        synchronizeModel.setUsername(username);
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        return habitAPI.synchronize(token, synchronizeModel);
    }

    @Override
    public Single<ResponseModel<HabitModel>> getAllHabit() {
        String username = DataLocalManager.getInstance().getUserName();
        String token = StringConstant.STRING_BEARER + DataLocalManager.getInstance().getToken();
        Log.d("RemoteHabitDataSource", "getAllHabit: " + username + "-- " + token);
        return habitAPI.getAllHabit(token, username);
    }

}
