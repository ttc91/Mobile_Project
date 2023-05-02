package com.example.back_end_mobile_project.data.remote.service.impl;

import com.example.back_end_mobile_project.data.constant.MessageConstant;
import com.example.back_end_mobile_project.data.mapper.HistoryMapper;
import com.example.back_end_mobile_project.data.model.collection.HistoryCollection;
import com.example.back_end_mobile_project.data.model.dto.HistoryDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.model.dto.request.GetAllHistoryRequestDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHistoryRequestDTO;
import com.example.back_end_mobile_project.data.remote.repository.HabitRepository;
import com.example.back_end_mobile_project.data.remote.repository.HistoryRepository;
import com.example.back_end_mobile_project.data.remote.service.HistoryService;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HistoryServiceImpl extends BaseService<BaseDTO> implements HistoryService {

    @Autowired
    HistoryMapper mapper;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    HabitRepository habitRepository;

    @Override
    public ResponseDTO<BaseDTO> getAll(BaseDTO dto) {
        try{

            List<HistoryDTO> historyDTOList = mapper.
                    mapToDTOList(historyRepository.findAllByUsername(((GetAllHistoryRequestDTO) dto).getUsername()), HistoryDTO.class);

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HistoryMessage.DATA_GET_HISTORY_BY_USERNAME_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .objectList(historyDTOList)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HistoryMessage.DATA_GET_HISTORY_BY_USERNAME_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }

    @Override
    public void create(String username, HistoryDTO dto) throws Exception {

        if(historyRepository.findHistoryCollectionByUsernameAndHabitIdAndHistoryDate(
                username, dto.getHabitId(), dto.getHistoryDate()) != null){
            throw new Exception();
        }

        HistoryCollection collection = mapper.mapToCollection(dto, HistoryCollection.class);
        collection.setUsername(username);
        historyRepository.insert(collection);

    }

    @Override
    public void createList(SynchronizeHistoryRequestDTO dto) {

        String username = dto.getUsername();
        List<HistoryDTO> historyDTOList = dto.getDataList();
        historyDTOList.forEach(historyDTO -> {
            try{
                create(username, historyDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }

    @Override
    public void update(String username, HistoryDTO dto) {
        HistoryCollection collection = historyRepository.findHistoryCollectionByUsernameAndHabitIdAndHistoryDate(
                username, dto.getHabitId(), dto.getHistoryDate());
        collection.setHistoryDate(dto.getHistoryDate());
        historyRepository.save(collection);
    }

    @Override
    public void delete(HistoryCollection collection) {
        historyRepository.delete(collection);
    }

    @Override
    public Set<Long> setHabitIdSet(List<HistoryDTO> dto) {
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

            String username = ((SynchronizeHistoryRequestDTO) dto).getUsername();
            List<HistoryDTO> historyDTOList = ((SynchronizeHistoryRequestDTO) dto).getDataList();

            Set<Long> habitIdSet = setHabitIdSet(historyDTOList);
            if(!checkValidHabitInWeekList(username, habitIdSet)){
                throw new Exception();
            }

            List<HistoryCollection> historyCollectionList = historyRepository.findAllByUsername(username);
            if(historyCollectionList.size() == 0){
                createList((SynchronizeHistoryRequestDTO) dto);
            }else {
                historyCollectionList.forEach(c -> {
                    boolean checkExistInAndroidDB = false;
                    for(int i = 0; i < historyDTOList.size(); i++){
                        if(c.getUsername().equals(username) &&
                            c.getHabitId().equals(historyDTOList.get(i).getHabitId()) &&
                                c.getHistoryDate().equals(historyDTOList.get(i).getHistoryDate())){
                            checkExistInAndroidDB = true;
                            update(username, historyDTOList.get(i));
                            historyDTOList.remove(i);
                            break;
                        }
                    }
                    if(!checkExistInAndroidDB){
                        delete(c);
                    }
                });

                historyDTOList.forEach(d -> {
                    try{
                        create(username, d);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HistoryMessage.DATA_SYNCHRONIZE_HISTORY_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HistoryMessage.DATA_SYNCHRONIZE_HISTORY_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }
}
