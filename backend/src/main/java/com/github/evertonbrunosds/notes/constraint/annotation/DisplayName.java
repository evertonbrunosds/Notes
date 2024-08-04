package com.github.evertonbrunosds.notes.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@NotBlank
@Length(max = 32, message = "The length must be less than or equal to 32")
@Pattern(regexp = "^(?! )(?!.*  )[a-zA-Z0-9 ]+(?<! )$", message = "It can only contain letters and numbers, space can also be used, but only between letters and numbers")
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayName {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
