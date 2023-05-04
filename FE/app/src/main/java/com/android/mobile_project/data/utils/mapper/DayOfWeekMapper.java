package com.android.mobile_project.data.utils.mapper;

import com.android.mobile_project.data.local.sqlite.entity.db.DayOfWeekEntity;
import com.android.mobile_project.data.remote.model.DayOfWeekModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

public class DayOfWeekMapper extends BaseMapper<DayOfWeekEntity, DayOfWeekMapper> implements EntityMapper<DayOfWeekEntity, DayOfWeekModel> {

    private static volatile DayOfWeekMapper instance;

    public static DayOfWeekMapper getInstance(){
        if(instance == null){
            instance = new DayOfWeekMapper();
        }
        return instance;
    }

    @Override
    public DayOfWeekModel mapToModel(DayOfWeekEntity e) {
        return new DayOfWeekModel(e.dayOfWeekId, e.dayOfWeekName);
    }

    @Override
    public DayOfWeekEntity mapToEntity(DayOfWeekModel m) {
        return new DayOfWeekEntity(m.getDayOfWeekId(), m.getDayOfWeekName());
    }
}
