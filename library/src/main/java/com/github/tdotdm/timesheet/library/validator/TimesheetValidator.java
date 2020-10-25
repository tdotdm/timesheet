package com.github.tdotdm.timesheet.library.validator;

import com.github.tdotdm.timesheet.library.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public final class TimesheetValidator {
    public List<String> validate(final Timesheet timesheet) {
        final List<String> errors = new ArrayList<>();
        validateYears(timesheet, errors);
        validateWeeks(timesheet, errors);
        validateDays(timesheet, errors);
        validateActions(timesheet, errors);

        return errors;
    }

    private void validateYears(final Timesheet timesheet,
                               final List<String> errors) {
        final List<Year> years = timesheet.getYears();
        if (years.isEmpty()) {
            errors.add("No years present in time sheet.");
        }

        final int size = years.size();
        if (size > 1) {
            final Year lastYear = years.get(size - 1);
            final Year secondLastEntry = years.get(size - 2);

            final int lastYearValue = lastYear.getValue();
            final int secondLastEntryValue = secondLastEntry.getValue();
            if (lastYearValue == secondLastEntryValue) {
                errors.add("Two sequential years have the same value.");
            }

            if (lastYearValue < secondLastEntryValue) {
                errors.add("Value of years are not in order.");
            }
        }
    }

    private void validateWeeks(final Timesheet timesheet,
                               final List<String> errors) {
        final List<Year> years = timesheet.getYears();
        for (Year year : years) {
            final List<Week> weeks = year.getWeeks();
            if (weeks.isEmpty()) {
                errors.add("No weeks present in year.");
            }

            final int size = weeks.size();
            if (size > 1) {
                final Week lastWeek = weeks.get(size - 1);
                final Week secondLastWeek = weeks.get(size - 2);

                final int lastWeekValue = lastWeek.getWeekNumber();
                final int secondLastWeekValue = secondLastWeek.getWeekNumber();
                if (lastWeekValue == secondLastWeekValue) {
                    errors.add("Two sequential weeks have the same week number.");
                }

                if (lastWeekValue < secondLastWeekValue) {
                    errors.add("Week numbers are not in order.");
                }
            }
        }
    }

    private void validateDays(final Timesheet timesheet,
                              final List<String> errors) {
        for (Year year : timesheet.getYears()) {
            final List<Week> weeks = year.getWeeks();
            for (Week week : weeks) {
                final List<Day> days = week.getDays();
                if (days.isEmpty()) {
                    errors.add("No days present in time sheet.");
                }

                final int size = days.size();
                if (size > 1) {
                    final Day lastDay = days.get(size - 1);
                    final Day secondLastDay = days.get(size - 2);

                    final DayOfWeek lastYearValue = lastDay.getDay();
                    final DayOfWeek secondLastEntryValue = secondLastDay.getDay();
                    if (lastYearValue == secondLastEntryValue) {
                        errors.add("Two sequential days have the same value.");
                    }
                }
            }
        }
    }

    private void validateActions(final Timesheet timesheet,
                                 final List<String> errors) {
        for (Year year : timesheet.getYears()) {
            final List<Week> weeks = year.getWeeks();
            for (Week week : weeks) {
                final List<Day> days = week.getDays();
                for (Day day : days) {
                    final List<Action> entries = day.getEntries();
                    if (entries.isEmpty()) {
                        errors.add("No entries present in time sheet.");
                    }

                    final int size = entries.size();
                    if (size > 1) {
                        final Action lastAction = entries.get(size - 1);
                        final Action secondLastAction = entries.get(size - 2);

                        final Action.Type lastYearValue = lastAction.getType();
                        final Action.Type secondLastEntryValue = secondLastAction.getType();
                        if (lastYearValue == secondLastEntryValue) {
                            errors.add("Two sequential actions have the same value.");
                        }
                    }
                }
            }
        }
    }
}