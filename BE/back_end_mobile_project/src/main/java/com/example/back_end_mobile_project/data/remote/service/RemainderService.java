package com.example.back_end_mobile_project.data.remote.service;

import com.example.back_end_mobile_project.data.model.collection.RemainderCollection;
import com.example.back_end_mobile_project.data.model.dto.RemainderDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeRemainderRequestDTO;

import java.util.List;
import java.util.Set;

public interface RemainderService {

    void create(String username, RemainderDTO dto) throws Exception;
    void createList(SynchronizeRemainderRequestDTO dto);
    void deleteAll(String username);
    Set<Long> setHabitIdSet(List<RemainderDTO> dto);
    Boolean checkValidHabitInWeekList(String username, Set<Long> habitIdSet);

}
