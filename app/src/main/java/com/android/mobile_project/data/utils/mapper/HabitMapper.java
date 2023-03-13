package com.android.mobile_project.data.utils.mapper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.utils.mapper.base.BaseMapper;
import com.android.mobile_project.data.utils.mapper.base.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class HabitMapper extends BaseMapper<HabitEntity, HabitModel> implements EntityMapper<HabitEntity, HabitModel> {

    private static volatile HabitMapper INSTANCE;

    public static HabitMapper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new HabitMapper();
        }
        return INSTANCE;
    }

    @Override
    public HabitModel mapToModel(HabitEntity e) {
        return new HabitModel(e.getHabitId(), e.getHabitName(), e.getHabitLogo(),
                e.getNumOfLongestSteak(), e.getUserId(), e.getDayOfTimeId());
    }

    @Override
    public HabitEntity mapToEntity(HabitModel m) {
        return new HabitEntity(m.getHabitId(), m.getHabitName(), m.getHabitLogo(),
                m.getNumOfLongestSteak(), m.getUserId(), m.getDayOfTimeId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<HabitEntity> mapToListEntity(List<HabitModel> models) {
        return models.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<HabitModel> mapToListModel(List<HabitEntity> entities) {
        return entities.stream().map(this::mapToModel).collect(Collectors.toList());
    }

}
