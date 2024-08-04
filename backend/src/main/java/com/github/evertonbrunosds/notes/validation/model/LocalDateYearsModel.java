package com.github.evertonbrunosds.notes.validation.model;

import static com.github.evertonbrunosds.notes.util.LocalDateManager.getYears;

import java.time.LocalDate;

import com.github.evertonbrunosds.notes.validation.annotation.LocalDateYears;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalDateYearsModel implements ConstraintValidator<LocalDateYears, LocalDate> {

    private int min;

    private int max;

    private boolean nullable;

    @Override
    public void initialize(final LocalDateYears year) {
        min = year.min();
        max = year.max();
        nullable = year.nullable();
    }

    @Override
    public boolean isValid(final LocalDate date, final ConstraintValidatorContext context) {
        if (date == null) {
            return nullable;
        }
        final var age = getYears(date);
        return age >= min && age <= max;
    }

}
