package com.android.mobile_project.data.remote.model.api.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class ResponseModel <T> implements Serializable {

    private String message;

    private Integer statusCode;

    private T object;

    private Exception messageError;

    private List<T> objectList = new ArrayList<>();

}