package com.quickstart.kitchensink.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class) // Link to validator logic
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "Email is already in use"; // Default error message

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
