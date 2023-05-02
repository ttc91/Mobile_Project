package com.android.mobile_project.data.utils.mapper.base;

import java.util.List;

public abstract class BaseMapper<E, M> {

    public List<M> mapToListModel(List<E> entities){
        return null;
    }

    public List<E> mapToListEntity(List<M> models){
        return null;
    }

}
