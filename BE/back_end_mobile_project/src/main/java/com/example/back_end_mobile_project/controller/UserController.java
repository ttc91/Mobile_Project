package com.example.back_end_mobile_project.controller;

import com.example.back_end_mobile_project.data.model.dto.UserDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.remote.service.UserService;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = ApiPaths.API_HOST_DOMAIN + ApiPaths.API_USER_DOMAIN)
public class UserController {

    @Qualifier("userServiceImpl")
    @Autowired
    BaseService service;

    @PostMapping(value = ApiPaths.API_CREATE_COLLECTION_DOMAIN)
    public ResponseEntity<ResponseDTO<BaseDTO>> create(@RequestBody UserDTO request){
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @GetMapping(value = ApiPaths.API_GET_ALL_COLLECTION_DOMAIN)
    public ResponseEntity<ResponseDTO<BaseDTO>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = ApiPaths.API_GET_ONE_COLLECTION_DOMAIN + "/{username}")
    public ResponseEntity<ResponseDTO<BaseDTO>> getOne(@PathVariable UserDTO username){
        return new ResponseEntity<>(service.getOne(username), HttpStatus.OK);
    }

    @PostMapping(value = ApiPaths.API_SIGN_IN_DOMAIN)
    public ResponseEntity<ResponseDTO<BaseDTO>> signIn(@RequestBody UserDTO request){
        return new ResponseEntity<>(((UserService)service).signIn(request), HttpStatus.OK);
    }

    @PostMapping(value = ApiPaths.API_REFRESH_TOKEN_DOMAIN)
    public ResponseEntity<ResponseDTO<BaseDTO>> refreshToken(HttpServletRequest request){
        return new ResponseEntity<>(((UserService)service).refreshToken(request), HttpStatus.OK);
    }

}
