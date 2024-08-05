package com.github.evertonbrunosds.notes.validation.annotation;

import org.hibernate.validator.constraints.Length;

@Length(max = 1024, message = "The length must be less than or equal to 1024")
public @interface Description {

}
