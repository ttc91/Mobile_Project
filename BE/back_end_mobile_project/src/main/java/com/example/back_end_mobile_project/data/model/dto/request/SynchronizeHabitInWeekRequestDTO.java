package com.example.back_end_mobile_project.data.model.dto.request;

import com.example.back_end_mobile_project.data.model.dto.HabitInWeekDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.utils.config.validation.name.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SynchronizeHabitInWeekRequestDTO extends BaseDTO {

    @ValidUsername
    @NotNull
    private String username;

    @NotNull
    List<HabitInWeekDTO> dataList;

}
