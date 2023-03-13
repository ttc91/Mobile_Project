package com.android.mobile_project.data.utils.mapper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryMapper extends BaseMapper<HistoryEntity, HistoryModel> implements EntityMapper<HistoryEntity, HistoryModel> {

    private static volatile HistoryMapper INSTANCE;

    public static HistoryMapper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new HistoryMapper();
        }
        return INSTANCE;
    }

    @Override
    public HistoryModel mapToModel(HistoryEntity e) {
        return new HistoryModel(e.getHistoryId(), e.getHistoryDate(), e.getHistoryHabitsState(), e.getUserId(), e.getHabitId());
    }

    @Override
    public HistoryEntity mapToEntity(HistoryModel m) {
        return new HistoryEntity(m.getHistoryId(), m.getHistoryDate(), m.getHistoryHabitsState(), m.getUserId(), m.getHabitId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<HistoryModel> mapToListModel(List<HistoryEntity> entities) {
        return entities.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<HistoryEntity> mapToListEntity(List<HistoryModel> models) {
        return models.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

}
