package com.android.mobile_project.data.utils.mapper.base;

public interface EntityMapper<Entity, Model>{
    Model mapToModel (Entity e);
    Entity mapToEntity (Model m);
}
