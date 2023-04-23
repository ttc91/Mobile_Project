package com.android.mobile_project.data.utils.mapper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfTimeEntity;
import com.android.mobile_project.data.remote.model.DayOfTimeModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class DayOfTimeMapper extends BaseMapper<DayOfTimeEntity, DayOfTimeModel>
        implements EntityMapper<DayOfTimeEntity, DayOfTimeModel> {

    private static volatile DayOfTimeMapper INSTANCE;

    public static DayOfTimeMapper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DayOfTimeMapper();
        }
        return INSTANCE;
    }

    @Override
    public DayOfTimeModel mapToModel(DayOfTimeEntity e) {
        return new DayOfTimeModel(e.dayOfTimeId, e.dayOfTimeName);
    }

    @Override
    public DayOfTimeEntity mapToEntity(DayOfTimeModel m) {
        return new DayOfTimeEntity(m.getDayOfTimeId(), m.getDayOfTimeName());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<DayOfTimeModel> mapToListModel(List<DayOfTimeEntity> entities) {
        return entities.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<DayOfTimeEntity> mapToListEntity(List<DayOfTimeModel> models) {
        return models.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
