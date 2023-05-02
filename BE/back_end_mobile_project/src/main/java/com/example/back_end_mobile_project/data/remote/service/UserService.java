package com.example.back_end_mobile_project.data.remote.service;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    ResponseDTO<BaseDTO> signIn(BaseDTO dto);
    ResponseDTO<BaseDTO> refreshToken(HttpServletRequest request);

}
