package com.github.evertonbrunosds.notes.util;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;

public interface LocalDateTimeManager {

    public static LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static int getYears(final LocalDateTime birthday) {
        return Period.between(birthday.toLocalDate(), currentLocalDateTime().toLocalDate()).getYears();
    }

    public static int getMonths(final LocalDateTime birthday) {
        return Period.between(birthday.toLocalDate(), currentLocalDateTime().toLocalDate()).getMonths();
    }

    public static int getDays(final LocalDateTime birthday) {
        return Period.between(birthday.toLocalDate(), currentLocalDateTime().toLocalDate()).getDays();
    }

}
