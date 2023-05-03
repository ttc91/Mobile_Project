package com.example.back_end_mobile_project.data.remote.repository;

import com.example.back_end_mobile_project.data.model.collection.HabitInWeekCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitInWeekRepository extends MongoRepository<HabitInWeekCollection, String> {

    HabitInWeekCollection findHabitInWeekCollectionByUsernameAndHabitIdAndDayOfWeekName(String username, Long habitId, String dayOfWeekName);
    void deleteAllByUsernameAndHabitId(String username, Long habitId);
    void deleteAllByUsername(String username);
    List<HabitInWeekCollection> findAllByUsername(String username);

}
