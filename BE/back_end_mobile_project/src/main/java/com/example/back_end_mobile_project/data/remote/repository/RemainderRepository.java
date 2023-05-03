package com.example.back_end_mobile_project.data.remote.repository;

import com.example.back_end_mobile_project.data.model.collection.RemainderCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RemainderRepository extends MongoRepository<RemainderCollection, String> {

    RemainderCollection findRemainderCollectionByUsernameAndHabitId(String username, Long habitId);
    void deleteAllByUsernameAndHabitId(String username, Long habitId);
    void deleteAllByUsername(String username);
    List<RemainderCollection> findAllByUsername(String username);

}
