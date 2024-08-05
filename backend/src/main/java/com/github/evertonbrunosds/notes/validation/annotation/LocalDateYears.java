package com.github.evertonbrunosds.notes.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.evertonbrunosds.notes.validation.model.LocalDateYearsModel;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = LocalDateYearsModel.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDateYears {

    String message() default "The local date does not have a valid number of years";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;

    boolean nullable() default true;

}
