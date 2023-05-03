package com.example.back_end_mobile_project.data.remote.service.impl;

import com.example.back_end_mobile_project.data.constant.MessageConstant;
import com.example.back_end_mobile_project.data.mapper.HabitMapper;
import com.example.back_end_mobile_project.data.model.collection.HabitCollection;
import com.example.back_end_mobile_project.data.model.dto.HabitDTO;
import com.example.back_end_mobile_project.data.model.dto.base.BaseDTO;
import com.example.back_end_mobile_project.data.model.dto.base.ResponseDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHabitRequestDTO;
import com.example.back_end_mobile_project.data.model.dto.request.GetAllHabitByUsernameRequestDTO;
import com.example.back_end_mobile_project.data.remote.repository.HabitInWeekRepository;
import com.example.back_end_mobile_project.data.remote.repository.HabitRepository;
import com.example.back_end_mobile_project.data.remote.repository.HistoryRepository;
import com.example.back_end_mobile_project.data.remote.repository.RemainderRepository;
import com.example.back_end_mobile_project.data.remote.service.HabitService;
import com.example.back_end_mobile_project.data.remote.service.base.BaseService;
import com.example.back_end_mobile_project.utils.ResponseCode;
import com.example.back_end_mobile_project.utils.time.DayOfTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HabitServiceImpl extends BaseService<BaseDTO> implements HabitService {

    @Autowired
    HabitMapper mapper;

    @Autowired
    HabitRepository habitRepository;

    @Autowired
    HabitInWeekRepository habitInWeekRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    RemainderRepository remainderRepository;

    @Override
    public ResponseDTO<BaseDTO> getAll(BaseDTO dto) {

        try {
            List<HabitCollection> habitCollectionList = habitRepository
                    .findAllByUsername(((GetAllHabitByUsernameRequestDTO) dto).getUsername());
            List<HabitDTO> habitDTOList = mapper.mapToDTOList(habitCollectionList, HabitDTO.class);

            habitDTOList.forEach(habitDTO -> {
                int index = habitDTOList.indexOf(habitDTO);
                habitDTO.setDateOfTime(DayOfTimeUtils.getInstance().getDayOfTimeIdByTimeName(habitCollectionList.get(index).getDayOfTime()));
            });

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitMessage.DATA_GET_HABIT_LIST_BY_USERNAME_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .objectList(habitDTOList)
                    .build();

        } catch (Exception e) {
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitMessage.DATA_GET_HABIT_LIST_BY_USERNAME_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public void create(String username, HabitDTO dto) throws Exception {

        if (habitRepository.findByUsernameAndHabitId(username, dto.getHabitId()) != null) {
            throw new Exception();
        }

        HabitCollection habitCollection = mapper.mapToCollection(dto, HabitCollection.class);
        habitCollection.setUsername(username);
        habitCollection.setDayOfTime(DayOfTimeUtils.getInstance().getDayOfTimeNameById(dto.getDateOfTime()));
        habitRepository.insert(habitCollection);

    }

    @Override
    public void createList(SynchronizeHabitRequestDTO dto) {
        String username = dto.getUsername();
        List<HabitDTO> habitDTOList = dto.getDataList();
        habitDTOList.forEach(habitDTO -> {
            try {
                create(username, habitDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update(HabitDTO dto) {
        HabitCollection habitCollection = habitRepository.findByUsernameAndHabitId(dto.getUsername(), dto.getHabitId());
        habitCollection.setHabitLogo(dto.getHabitLogo());
        habitCollection.setHabitName(dto.getHabitName());
        habitCollection.setDayOfTime(DayOfTimeUtils.getInstance().getDayOfTimeNameById(dto.getDateOfTime()));
        habitCollection.setNumOfLongestSteak(dto.getNumOfLongestSteak());
        habitRepository.save(habitCollection);
    }

    @Override
    public void delete(HabitCollection collection) {
        String username = collection.getUsername();
        Long habitId = collection.getHabitId();
        habitRepository.delete(collection);
        habitInWeekRepository.deleteAllByUsernameAndHabitId(username, habitId);
        historyRepository.deleteAllByUsernameAndHabitId(username, habitId);
        remainderRepository.deleteAllByUsernameAndHabitId(username, habitId);
    }

    @Override
    public ResponseDTO<BaseDTO> synchronize(BaseDTO dto) {

        try{
            String username = ((SynchronizeHabitRequestDTO) dto).getUsername();
            List<HabitDTO> habitDTOList = ((SynchronizeHabitRequestDTO) dto).getDataList();
            List<HabitCollection> habitCollectionList = habitRepository.findAllByUsername(username);

            if(habitCollectionList.size() == 0){
                createList((SynchronizeHabitRequestDTO) dto);
            }else {
                habitCollectionList.forEach(habitCollection -> {
                    boolean checkExistInAndroidDB = false;
                    for(int i = 0; i < habitDTOList.size(); i++){
                        if(habitCollection.getHabitId().equals(habitDTOList.get(i).getHabitId())){
                            checkExistInAndroidDB = true;
                            update(habitDTOList.get(i));
                            habitDTOList.remove(i);
                            break;
                        }
                    }
                    if(!checkExistInAndroidDB){
                        delete(habitCollection);
                    }
                });
            }

            habitDTOList.forEach(habitDTO -> {
                try {
                    create(username, habitDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitMessage.DATA_SYNCHRONIZE_HABIT_COMPLETE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_OK_CODE)
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.<BaseDTO>builder()
                    .message(MessageConstant.HabitMessage.DATA_SYNCHRONIZE_HABIT_FAILURE)
                    .createdTime(LocalDateTime.now())
                    .statusCode(ResponseCode.RESPONSE_ERROR_SERVER_ERROR)
                    .messageError(e)
                    .build();
        }
    }
}

