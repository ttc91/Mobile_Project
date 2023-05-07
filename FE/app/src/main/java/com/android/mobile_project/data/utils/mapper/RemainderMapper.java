package com.android.mobile_project.data.utils.mapper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class RemainderMapper extends BaseMapper<RemainderEntity, RemainderModel> implements EntityMapper<RemainderEntity, RemainderModel> {

    private static volatile RemainderMapper INSTANCE;

    public static RemainderMapper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RemainderMapper();
        }
        return INSTANCE;
    }


    @Override
    public RemainderModel mapToModel(RemainderEntity e) {
        return new RemainderModel(e.remainderId, e.habitId, e.hourTime, e.minutesTime);
    }

    @Override
    public RemainderEntity mapToEntity(RemainderModel m) {

        RemainderEntity entity = new RemainderEntity();
        entity.habitId = m.getHabitId();
        entity.remainderId = m.getRemainderId();
        entity.hourTime = m.getHourTime();
        entity.minutesTime = m.getMinutesTime();

        return entity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<RemainderModel> mapToListModel(List<RemainderEntity> entities) {
        return entities.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<RemainderEntity> mapToListEntity(List<RemainderModel> models) {
        return models.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

}
