package com.example.back_end_mobile_project.data.model.dto;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemainderDTO extends BaseDTO {

    private String username;

    private Long habitId;

    private Long hourTime;

    private Long minutesTime;

}
