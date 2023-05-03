package com.example.back_end_mobile_project.controller;

import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.model.dto.request.GetAllHabitInWeekByUsernameRequestDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHabitInWeekRequestDTO;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ApiPaths;
import com.example.back_end_mobile_project.utils.config.validation.name.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiPaths.API_HOST_DOMAIN + ApiPaths.API_HABIT_IN_WEEK_DOMAIN)
@Validated
public class HabitInWeekController {

    @Qualifier("habitInWeekServiceImpl")
    @Autowired
    BaseService service;

    @GetMapping(value = ApiPaths.API_GET_ALL_COLLECTION_DOMAIN + "/{username}")
    public ResponseEntity<ResponseDTO<BaseDTO>> getAllByUsername(@PathVariable @ValidUsername String username){
        GetAllHabitInWeekByUsernameRequestDTO dto = new GetAllHabitInWeekByUsernameRequestDTO(username);
        return new ResponseEntity<ResponseDTO<BaseDTO>>(service.getAll(dto), HttpStatus.OK);
    }

    @PostMapping(value = ApiPaths.API_SYNCHRONIZE_DOMAIN)
    public ResponseEntity<ResponseDTO<BaseDTO>> synchronize(@Valid @RequestBody SynchronizeHabitInWeekRequestDTO request){
        return new ResponseEntity<ResponseDTO<BaseDTO>>(service.synchronize(request), HttpStatus.OK);
    }


}
