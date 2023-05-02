package com.example.back_end_mobile_project.data.model.dto;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDTO extends BaseDTO {

    private String historyDate;

    private String historyHabitsState;

    private String username;

    private Long habitId;

}
