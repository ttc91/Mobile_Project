package com.example.back_end_mobile_project.data.model.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> implements Serializable {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private String message;

    private Integer statusCode;

    private T object;

    private Exception messageError;

    private List<?> objectList;

    @JsonFormat(pattern = FORMAT_DATE)
    private LocalDateTime createdTime;

}
