package com.github.evertonbrunosds.notes.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;

public interface LocalDateManager {

    public static LocalDate currentLocalDate() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    public static int getYears(final LocalDate birthday) {
        return Period.between(birthday, currentLocalDate()).getYears();
    }

    public static int getMonths(final LocalDate birthday) {
        return Period.between(birthday, currentLocalDate()).getMonths();
    }

    public static int getDays(final LocalDate birthday) {
        return Period.between(birthday, currentLocalDate()).getDays();
    }

}
