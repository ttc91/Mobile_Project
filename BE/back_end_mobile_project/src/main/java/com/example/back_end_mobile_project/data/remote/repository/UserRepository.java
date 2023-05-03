package com.example.back_end_mobile_project.data.remote.repository;

import com.example.back_end_mobile_project.data.model.collection.UserCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserCollection, String> {

    UserCollection getUserCollectionsByUsername(String username);

}
