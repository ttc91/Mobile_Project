package com.example.back_end_mobile_project.utils.config.validation;

import com.example.back_end_mobile_project.data.remote.repository.UserRepository;
import com.example.back_end_mobile_project.utils.config.validation.name.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    UserRepository repository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return repository.getUserCollectionsByUsername(s) != null;
    }

}
