package com.example.back_end_mobile_project.data.mapper.base;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<C, D> {

    @Autowired
    ModelMapper mapper;

    public D mapToDTO (C c, Class<D> d) {
        return c != null ? mapper.map(c, d) : null;
    }

    public C mapToCollection(D d, Class<C> c){
        return d != null ? mapper.map(d, c) : null;
    }

    public List<D> mapToDTOList(List<C> cList, Class<D> d){
        return cList != null ? cList.stream().map(c -> mapper.map(c, d)).collect(Collectors.toList()) : null;
    }

    public List<C> mapToCollectionList(List<D> dList, Class<C> c){
        return dList != null ? dList.stream().map(d -> mapper.map(d, c)).collect(Collectors.toList()) : null;
    }

}
