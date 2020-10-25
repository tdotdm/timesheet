package com.github.tdotdm.timesheet.library.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class Year {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private UUID id;
    private String timestamp;
    private double totalHours;
    private int value;
    private final List<Week> weeks = new ArrayList<>();

    public Year(final LocalDateTime localDateTime,
                final int value) {
        this.id = UUID.randomUUID();
        this.timestamp = localDateTime.format(DATE_TIME_FORMATTER);
        this.value = value;
    }

    public void addWeek(final Week week) {
        this.weeks.add(week);
    }
}
