package com.example.back_end_mobile_project.data.model.dto;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.utils.config.validation.name.ValidDateOfTime;
import com.example.back_end_mobile_project.utils.config.validation.name.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitDTO extends BaseDTO {

    @NotNull
    @ValidUsername
    private String username;

    @NotNull
    private Long habitId;

    @NotNull
    private String habitName;

    private String habitLogo;

    @NotNull
    private Long numOfLongestSteak;

    @NotNull
    @ValidDateOfTime
    private Long dateOfTime;

}
