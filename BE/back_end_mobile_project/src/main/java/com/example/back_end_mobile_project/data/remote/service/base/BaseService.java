package com.example.back_end_mobile_project.data.remote.service.base;

import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;

public abstract class BaseService<T> {

    public ResponseDTO<T> create (T t) { return  null; }
    public ResponseDTO<T> update (T t) { return null; }
    public ResponseDTO<T> delete (T t) { return null; }
    public ResponseDTO<T> getOne (T t) { return null; }
    public ResponseDTO<T> getAll () { return null; }
    public ResponseDTO<T> getAll(T t) { return null; }
    public ResponseDTO<T> synchronize(T t) { return null; }

}
