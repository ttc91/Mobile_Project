package com.example.back_end_mobile_project.data.model.dto.request;

import com.example.back_end_mobile_project.data.model.dto.RemainderDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.utils.config.validation.name.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SynchronizeRemainderRequestDTO  extends BaseDTO {

    @ValidUsername
    @NotNull
    private String username;

    @NotNull
    private List<RemainderDTO> dataList;

}
