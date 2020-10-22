package com.github.tdotdm.timesheet.library;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class Record {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private UUID id;
    private String dateCreated;
    private int weekNumber;
    private DayOfWeek dayOfWeek;
    private final List<Entry> entries = new ArrayList<>();

    public Record(final LocalDateTime timestamp,
                  final int weekNumber,
                  final DayOfWeek dayOfWeek) {
        this.id = UUID.randomUUID();
        this.dateCreated = timestamp.format(DATE_TIME_FORMATTER);
        this.weekNumber = weekNumber;
        this.dayOfWeek = dayOfWeek;
    }

    public void addEntry(final Entry entry) {
        this.entries.add(entry);
    }
}
