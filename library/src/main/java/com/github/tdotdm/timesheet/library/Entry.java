package com.github.tdotdm.timesheet.library;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public final class Entry {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Action action;
    private final String timestamp; //e.g. 18:00:49

    public Entry(final Action action,
                 final LocalDateTime timestamp) {
        this.action = action;
        this.timestamp = timestamp.format(DATE_TIME_FORMATTER);
    }
}
