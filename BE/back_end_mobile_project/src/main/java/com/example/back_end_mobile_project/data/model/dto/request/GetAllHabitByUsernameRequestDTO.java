package com.example.back_end_mobile_project.data.model.dto.request;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllHabitByUsernameRequestDTO extends BaseDTO {

    private String username;

}
