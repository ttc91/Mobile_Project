package com.android.mobile_project.data.utils.mapper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class HabitInWeekMapper extends BaseMapper<HabitInWeekEntity, HabitInWeekModel> implements EntityMapper<HabitInWeekEntity, HabitInWeekModel> {

    private static volatile HabitInWeekMapper INSTANCE;

    public static HabitInWeekMapper getInstance(){
        if (INSTANCE == null){
            INSTANCE = new HabitInWeekMapper();
        }
        return INSTANCE;
    }

    @Override
    public HabitInWeekModel mapToModel(HabitInWeekEntity e) {
        return new HabitInWeekModel(e.getUserId(), e.getHabitId(), e.getDayOfWeekId(),
            e.getTimerHour(), e.getTimerMinute(), e.getTimerSecond());
    }

    @Override
    public HabitInWeekEntity mapToEntity(HabitInWeekModel m) {
        return  new HabitInWeekEntity(m.getUserId(), m.getHabitId(), m.getDayOfWeekId(),
            m.getTimerHour(), m.getTimerMinute(), m.getTimerSecond());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<HabitInWeekModel> mapToListModel(List<HabitInWeekEntity> entities) {
        return entities.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<HabitInWeekEntity> mapToListEntity(List<HabitInWeekModel> models) {
        return models.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

}
