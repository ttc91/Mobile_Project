package com.example.back_end_mobile_project.data.remote.repository;

import com.example.back_end_mobile_project.data.model.collection.HabitCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends MongoRepository<HabitCollection, String> {
    HabitCollection findByUsernameAndHabitId(String username, Long habitId);
    List<HabitCollection> findAllByUsername(String username);
}
