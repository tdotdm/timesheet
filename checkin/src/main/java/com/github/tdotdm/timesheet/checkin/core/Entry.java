package com.github.tdotdm.timesheet.checkin.core;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class Entry {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Action action;
    private final String timestamp; //e.g. 18:00:49

    public Entry(final Action action,
                 final LocalDateTime timestamp) {
        this.action = action;
        this.timestamp = timestamp.format(DATE_TIME_FORMATTER);
    }
}