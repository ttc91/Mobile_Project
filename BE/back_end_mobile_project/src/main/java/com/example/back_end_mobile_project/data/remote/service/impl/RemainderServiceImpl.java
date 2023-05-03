package com.example.back_end_mobile_project.data.remote.service.impl;

import com.example.back_end_mobile_project.data.constant.MessageConstant;
import com.example.back_end_mobile_project.data.mapper.RemainderMapper;
import com.example.back_end_mobile_project.data.model.collection.RemainderCollection;
import com.example.back_end_mobile_project.data.model.dto.RemainderDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.model.dto.request.GetAllRemainderRequestDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeRemainderRequestDTO;
import com.example.back_end_mobile_project.data.remote.repository.HabitRepository;
import com.example.back_end_mobile_project.data.remote.repository.RemainderRepository;
import com.example.back_end_mobile_project.data.remote.service.RemainderService;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RemainderServiceImpl extends BaseService<BaseDTO> implements RemainderService {

    @Autowired
    RemainderMapper mapper;

    @Autowired
    RemainderRepository remainderRepository;

    @Autowired
    HabitRepository habitRepository;

    @Override
    public ResponseDTO<BaseDTO> getAll(BaseDTO dto) {
        try{

            List<RemainderDTO> remainderDTOList = mapper
                    .mapToDTOList(remainderRepository.findAllByUsername(((GetAllRemainderRequestDTO) dto).getUsername()), RemainderDTO.class);

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.RemainderMessage.DATA_GET_REMAINDER_BY_USERNAME_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .objectList(remainderDTOList)
                    .build();

        }catch (Exception e){
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.RemainderMessage.DATA_GET_REMAINDER_BY_USERNAME_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .messageError(e)
                    .build();
        }
    }

    @Override
    public void create(String username, RemainderDTO dto) throws Exception {
        RemainderCollection remainderCollection = mapper.mapToCollection(dto, RemainderCollection.class);
        remainderCollection.setUsername(username);
        remainderRepository.insert(remainderCollection);
    }

    @Override
    public void createList(SynchronizeRemainderRequestDTO dto) {
        String username = dto.getUsername();
        List<RemainderDTO> remainderDTOList = dto.getDataList();
        remainderDTOList.forEach(d -> {
            try{
                create(username, d);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteAll(String username) {
        remainderRepository.deleteAllByUsername(username);
    }

    @Override
    public Set<Long> setHabitIdSet(List<RemainderDTO> dto) {
        Set<Long> set = new HashSet<>();
        dto.forEach(d -> {
            set.add(d.getHabitId());
        });
        return set;
    }

    @Override
    public Boolean checkValidHabitInWeekList(String username, Set<Long> habitIdSet) {
        for(Long habitId: habitIdSet){
            if(habitRepository.findByUsernameAndHabitId(username, habitId) == null){
                return false;
            }
        }
        return true;
    }

    @Override
    public ResponseDTO<BaseDTO> synchronize(BaseDTO dto) {
        try{
            String username = ((SynchronizeRemainderRequestDTO) dto).getUsername();
            List<RemainderDTO> remainderDTOList = ((SynchronizeRemainderRequestDTO) dto).getDataList();

            Set<Long> habitIdSet = setHabitIdSet(remainderDTOList);
            if(!checkValidHabitInWeekList(username, habitIdSet)){
                throw new Exception();
            }

            List<RemainderCollection> remainderCollectionList = remainderRepository.findAllByUsername(username);
            if(remainderCollectionList.size() == 0){
                createList((SynchronizeRemainderRequestDTO) dto);
            }else {
                deleteAll(username);
                createList((SynchronizeRemainderRequestDTO) dto);
            }

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.RemainderMessage.DATA_SYNCHRONIZE_REMAINDER_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.RemainderMessage.DATA_SYNCHRONIZE_REMAINDER_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }
}
