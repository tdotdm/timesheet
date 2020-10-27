package com.github.tdotdm.timesheet.library.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Data
public final class Week {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final WeekFields WEEK_FIELDS = WeekFields.of(Locale.getDefault());

    private UUID id;
    private String timestamp;
    private double totalHours;
    private int weekNumber;
    private final List<Day> days = new ArrayList<>();

    public Week(final LocalDateTime localDateTime) {
        this.id = UUID.randomUUID();
        this.timestamp = localDateTime.format(DATE_TIME_FORMATTER);
        this.weekNumber = localDateTime.get(WEEK_FIELDS.weekOfWeekBasedYear());
    }

    public void addDay(final Day day) {
        this.days.add(day);
    }
}
