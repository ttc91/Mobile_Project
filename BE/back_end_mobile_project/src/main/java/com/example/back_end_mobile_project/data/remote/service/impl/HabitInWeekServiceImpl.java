package com.example.back_end_mobile_project.data.remote.service.impl;

import com.example.back_end_mobile_project.data.constant.MessageConstant;
import com.example.back_end_mobile_project.data.mapper.HabitInWeekMapper;
import com.example.back_end_mobile_project.data.model.collection.HabitInWeekCollection;
import com.example.back_end_mobile_project.data.model.dto.HabitInWeekDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.model.dto.request.GetAllHabitInWeekByUsernameRequestDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHabitInWeekRequestDTO;
import com.example.back_end_mobile_project.data.remote.repository.HabitInWeekRepository;
import com.example.back_end_mobile_project.data.remote.repository.HabitRepository;
import com.example.back_end_mobile_project.data.remote.service.HabitInWeekService;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ResponseCode;
import com.example.back_end_mobile_project.utils.time.DayOfWeekUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HabitInWeekServiceImpl extends BaseService<BaseDTO> implements HabitInWeekService {

    @Autowired
    HabitInWeekMapper mapper;

    @Autowired
    HabitInWeekRepository habitInWeekRepository;

    @Autowired
    HabitRepository habitRepository;

    @Override
    public ResponseDTO<BaseDTO> getAll(BaseDTO dto) {
        try {
            List<HabitInWeekCollection> habitInWeekCollectionList = habitInWeekRepository.findAllByUsername(((GetAllHabitInWeekByUsernameRequestDTO) dto).getUsername());
            List<HabitInWeekDTO> habitInWeekDTOList = mapper.mapToDTOList(habitInWeekCollectionList, HabitInWeekDTO.class);

            habitInWeekDTOList.forEach(habitInWeekDTO -> {
                int index = habitInWeekDTOList.indexOf(habitInWeekDTO);
                habitInWeekDTO.setDayOfWeekId(DayOfWeekUtils.getInstance().getDayOfWeekIdByName(habitInWeekCollectionList.get(index).getDayOfWeekName()));
            });

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitInWeekMessage.DATA_GET_HABIT_IN_WEEK_LIST_BY_USERNAME_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .objectList(habitInWeekDTOList)
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitInWeekMessage.DATA_GET_HABIT_IN_WEEK_LIST_BY_USERNAME_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }

    @Override
    public void create(String username, HabitInWeekDTO dto) throws Exception {

        if(habitInWeekRepository.findHabitInWeekCollectionByUsernameAndHabitIdAndDayOfWeekName(
                username, dto.getHabitId(), DayOfWeekUtils.getInstance().getDayOfWeekNameById(dto.getDayOfWeekId())) != null) {
            throw new Exception();
        }

        HabitInWeekCollection habitInWeekCollection = mapper.mapToCollection(dto, HabitInWeekCollection.class);
        habitInWeekCollection.setUsername(username);
        habitInWeekCollection.setDayOfWeekName(DayOfWeekUtils.getInstance().getDayOfWeekNameById(dto.getDayOfWeekId()));
        habitInWeekRepository.insert(habitInWeekCollection);

    }

    @Override
    public void createList(SynchronizeHabitInWeekRequestDTO dto) {
        String username = dto.getUsername();
        List<HabitInWeekDTO> habitInWeekDTOList = dto.getDataList();
        habitInWeekDTOList.forEach(habitInWeekDTO -> {
            try {
                create(username, habitInWeekDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(String username, HabitInWeekDTO dto) {
        HabitInWeekCollection habitInWeekCollection = habitInWeekRepository.findHabitInWeekCollectionByUsernameAndHabitIdAndDayOfWeekName(
                username, dto.getHabitId(), DayOfWeekUtils.getInstance().getDayOfWeekNameById(dto.getDayOfWeekId()));
        habitInWeekCollection.setTimerHour(dto.getTimerHour());
        habitInWeekCollection.setTimerMinute(dto.getTimerMinute());
        habitInWeekCollection.setTimerSecond(dto.getTimerSecond());
        habitInWeekRepository.save(habitInWeekCollection);
    }

    @Override
    public void delete(HabitInWeekCollection collection) {
        habitInWeekRepository.delete(collection);
    }

    @Override
    public Set<Long> setHabitIdSet(List<HabitInWeekDTO> dto) {
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
        try {
            String username = ((SynchronizeHabitInWeekRequestDTO) dto).getUsername();
            List<HabitInWeekDTO> habitInWeekDTOList = ((SynchronizeHabitInWeekRequestDTO) dto).getDataList();

            Set<Long> habitIdSet = setHabitIdSet(habitInWeekDTOList);
            if(!checkValidHabitInWeekList(username, habitIdSet)){
                throw new Exception();
            }

            List<HabitInWeekCollection> habitInWeekCollectionList = habitInWeekRepository.findAllByUsername(username);
            if(habitInWeekCollectionList.size() == 0) {
                createList((SynchronizeHabitInWeekRequestDTO) dto);
            }else {
                habitInWeekCollectionList.forEach(habitInWeekCollection -> {
                    boolean checkExistInAndroidDB = false;
                    for(int i = 0; i < habitInWeekDTOList.size(); i++){
                        if(habitInWeekCollection.getUsername().equals(username) &&
                            habitInWeekCollection.getHabitId().equals(habitInWeekDTOList.get(i).getHabitId()) &&
                                habitInWeekCollection.getDayOfWeekName().equals(DayOfWeekUtils.getInstance()
                                        .getDayOfWeekNameById(habitInWeekDTOList.get(i).getDayOfWeekId()))){
                            checkExistInAndroidDB = true;
                            update(username, habitInWeekDTOList.get(i));
                            habitInWeekDTOList.remove(i);
                            break;
                        }
                    }
                    if(!checkExistInAndroidDB){
                        delete(habitInWeekCollection);
                    }
                });

                habitInWeekDTOList.forEach(habitInWeekDTO -> {
                    try{
                        create(username, habitInWeekDTO);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitInWeekMessage.DATA_SYNCHRONIZE_HABIT_IN_WEEK_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitInWeekMessage.DATA_SYNCHRONIZE_HABIT_IN_WEEK_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }

}
