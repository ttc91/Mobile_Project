package com.example.back_end_mobile_project.data.remote.service;

import com.example.back_end_mobile_project.data.model.collection.HistoryCollection;
import com.example.back_end_mobile_project.data.model.dto.HistoryDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHistoryRequestDTO;

import java.util.List;
import java.util.Set;

public interface HistoryService {

    void create(String username, HistoryDTO dto) throws Exception;
    void createList(SynchronizeHistoryRequestDTO dto);
    void update(String username, HistoryDTO dto);
    void delete(HistoryCollection collection);
    Set<Long> setHabitIdSet(List<HistoryDTO> dto);
    Boolean checkValidHabitInWeekList(String username, Set<Long> habitIdSet);

}
