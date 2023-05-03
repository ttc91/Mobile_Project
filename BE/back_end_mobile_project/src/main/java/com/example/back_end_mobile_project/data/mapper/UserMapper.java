package com.example.back_end_mobile_project.data.mapper;

import com.example.back_end_mobile_project.data.mapper.base.BaseMapper;
import com.example.back_end_mobile_project.data.model.collection.UserCollection;
import com.example.back_end_mobile_project.data.model.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<UserCollection, UserDTO> {
}
