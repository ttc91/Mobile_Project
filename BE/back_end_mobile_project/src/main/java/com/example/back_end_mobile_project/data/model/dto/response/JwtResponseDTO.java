package com.example.back_end_mobile_project.data.model.dto.response;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO extends BaseDTO implements Serializable {

    private String token;

}
