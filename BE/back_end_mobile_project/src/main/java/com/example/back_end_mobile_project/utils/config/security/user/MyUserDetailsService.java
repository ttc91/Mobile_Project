package com.example.back_end_mobile_project.utils.config.security.user;

import com.example.back_end_mobile_project.data.model.collection.UserCollection;
import com.example.back_end_mobile_project.data.remote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCollection userCollection = repository.getUserCollectionsByUsername(username);
        if(userCollection == null){
            throw  new UsernameNotFoundException(username);
        }
        return new MyUserDetails(userCollection);
    }
}
