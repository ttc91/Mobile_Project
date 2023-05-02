package com.example.back_end_mobile_project.data.remote.service.impl;

import com.example.back_end_mobile_project.data.constant.MessageConstant;
import com.example.back_end_mobile_project.data.mapper.UserMapper;
import com.example.back_end_mobile_project.data.model.collection.UserCollection;
import com.example.back_end_mobile_project.data.model.dto.UserDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.model.dto.response.JwtResponseDTO;
import com.example.back_end_mobile_project.data.remote.repository.UserRepository;
import com.example.back_end_mobile_project.data.remote.service.UserService;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ResponseCode;
import com.example.back_end_mobile_project.utils.config.security.MyAuthenticationManager;
import com.example.back_end_mobile_project.utils.config.security.jwt.JwtProvider;
import com.example.back_end_mobile_project.utils.config.security.user.MyUserDetailsService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService<BaseDTO> implements UserService {

    @Autowired
    UserMapper mapper;

    @Autowired
    UserRepository repository;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MyAuthenticationManager myAuthenticationManager;

    private static final String VALUE_NON_PWD = "NON_PASSWORD";

    @Override
    public ResponseDTO<BaseDTO> create(BaseDTO dto) {
        try{
            UserCollection userCollection = repository.insert(new UserCollection(((UserDTO) dto).getUsername()));
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_CREATE_USER_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_CREATED)
                    .object(mapper.mapToDTO(userCollection, UserDTO.class))
                    .build();
        }catch (Exception e){
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_CREATE_USER_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }

    @Override
    public ResponseDTO<BaseDTO> getOne(BaseDTO dto) {
        try{
            String username = ((UserDTO) dto).getUsername();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_GET_ALL_USER_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .object(mapper.mapToDTO(repository.getUserCollectionsByUsername(username), UserDTO.class))
                    .build();
        }catch (Exception e){
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_GET_ONE_USER_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public ResponseDTO<BaseDTO> getAll() {
        try{
            List<UserDTO> dtoList = mapper.mapToDTOList(repository.findAll(), UserDTO.class);
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_GET_ALL_USER_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .objectList(dtoList)
                    .build();
        }catch (Exception e){
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_GET_ALL_USER_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }

    @Override
    public ResponseDTO<BaseDTO> signIn(BaseDTO dto) {

        try{
            String username = ((UserDTO) dto).getUsername();
            UserCollection userCollection = repository.getUserCollectionsByUsername(username);

            if(userCollection == null){
                userCollection = new UserCollection(username);
                repository.save(userCollection);
            }

            myAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, VALUE_NON_PWD));

            final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            String token = jwtProvider.generateToken(userDetails);

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_SIGN_IN_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .object(new JwtResponseDTO(token))
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_SIGN_IN_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }

    }

    @Override
    public ResponseDTO<BaseDTO> refreshToken(HttpServletRequest request) {

        try {
            DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
            Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
            String token = jwtProvider.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_REFRESH_TOKEN_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .object(new JwtResponseDTO(token))
                    .build();
        }catch (Exception e){
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.UserMessage.DATA_REFRESH_TOKEN_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
