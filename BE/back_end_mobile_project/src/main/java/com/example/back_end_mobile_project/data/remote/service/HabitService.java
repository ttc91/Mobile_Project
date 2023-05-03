package com.example.back_end_mobile_project.data.remote.service;

import com.example.back_end_mobile_project.data.model.collection.HabitCollection;
import com.example.back_end_mobile_project.data.model.dto.HabitDTO;
import com.example.back_end_mobile_project.data.model.dto.request.SynchronizeHabitRequestDTO;

public interface HabitService {

    void create(String username, HabitDTO dto) throws Exception;
    void createList(SynchronizeHabitRequestDTO dto);
    void update(HabitDTO dto);
    void delete(HabitCollection collection);

}
