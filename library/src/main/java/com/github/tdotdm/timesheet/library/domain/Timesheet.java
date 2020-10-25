package com.github.tdotdm.timesheet.library.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public final class Timesheet {
    private final List<Year> years = new ArrayList<>();

    public Optional<Year> getLatestYear() {
        if (years.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(years.get(years.size() - 1));
    }

    public Optional<Week> getLatestWeek() {
        if (years.isEmpty()) {
            return Optional.empty();
        }

        final Year latestYear = getLatestYear().get();
        final List<Week> weeks = latestYear.getWeeks();
        if (weeks.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(weeks.get(weeks.size() - 1));
    }

    public Optional<Day> getLatestDay() {
        final Optional<Week> optionalLatestWeek = getLatestWeek();
        if (optionalLatestWeek.isEmpty()) {
            return Optional.empty();
        }

        final Week latestWeek = optionalLatestWeek.get();
        final List<Day> days = latestWeek.getDays();
        if (days.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(days.get(days.size() - 1));
    }

    public void addYear(final Year year) {
        this.years.add(year);
    }
}
