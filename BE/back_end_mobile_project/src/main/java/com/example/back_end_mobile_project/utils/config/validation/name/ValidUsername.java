package com.example.back_end_mobile_project.utils.config.validation.name;

import com.example.back_end_mobile_project.utils.config.validation.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface ValidUsername {
    String message() default "Username does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
