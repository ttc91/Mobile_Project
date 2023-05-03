package com.example.back_end_mobile_project.data.model.collection.base;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public abstract class BaseCollection {

    @Id
    private String id;

}
