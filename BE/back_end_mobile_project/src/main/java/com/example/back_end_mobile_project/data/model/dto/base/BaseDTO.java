package com.example.back_end_mobile_project.data.model.dto.base;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseDTO implements Serializable {

    private String id;

}
