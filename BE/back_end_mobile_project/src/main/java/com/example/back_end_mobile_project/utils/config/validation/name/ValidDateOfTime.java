package com.example.back_end_mobile_project.utils.config.validation.name;


import com.example.back_end_mobile_project.utils.config.validation.DayOfTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = DayOfTimeValidator.class)
@Documented
public @interface ValidDateOfTime {
    String message() default "Day of time id was not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
