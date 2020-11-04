package com.github.tdotdm.timesheet.library;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
final class Day {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private UUID id;
    private String timestamp;
    private double totalHours;
    private DayOfWeek day;
    private List<Action> entries = new ArrayList<>();

    Day(final LocalDateTime localDateTime) {
        this.id = UUID.randomUUID();
        this.timestamp = localDateTime.format(DATE_TIME_FORMATTER);
        this.day = localDateTime.getDayOfWeek();
    }

    void addEntry(final Action action) {
        this.entries.add(action);
    }
}
