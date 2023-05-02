package com.example.back_end_mobile_project.data.remote.service;

import com.example.back_end_mobile_project.data.model.collection.HabitInWeekCollection;
import com.example.back_end_mobile_project.data.model.dto.HabitInWeekDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHabitInWeekRequestDTO;

import java.util.List;
import java.util.Set;

public interface HabitInWeekService {

    void create(String username, HabitInWeekDTO dto) throws Exception;
    void createList(SynchronizeHabitInWeekRequestDTO dto);
    void update(String username, HabitInWeekDTO dto);
    void delete(HabitInWeekCollection collection);
    Set<Long> setHabitIdSet(List<HabitInWeekDTO> dto);
    Boolean checkValidHabitInWeekList(String username, Set<Long> habitIdSet);

}
