package com.example.back_end_mobile_project.utils.config.validation;

import com.example.back_end_mobile_project.utils.config.validation.name.ValidDateOfTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DayOfTimeValidator implements ConstraintValidator<ValidDateOfTime, Long> {

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return (aLong >= 1L && aLong <= 4L);
    }
}
