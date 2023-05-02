package com.example.back_end_mobile_project.data.remote.repository;

import com.example.back_end_mobile_project.data.model.collection.HistoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<HistoryCollection, String> {

    HistoryCollection findHistoryCollectionByUsernameAndHabitIdAndHistoryDate(String username, Long habitId, String historyDate);
    void deleteAllByUsername(String username);
    void deleteAllByUsernameAndHabitId(String username, Long habitId);
    List<HistoryCollection> findAllByUsername(String username);

}
