package com.github.tdotdm.timesheet.library.service;

import com.github.tdotdm.timesheet.library.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public final class CheckService {
    private final LocationService locationService;
    private final TimesheetService timesheetService;

    public void check(final Action.Type type) {
        final boolean workingDirectoryIsReady = isWorkingDirectoryReady();
        if (workingDirectoryIsReady) {
            final Timesheet timesheet = timesheetService.read();
            final LocalDateTime localDateTime = LocalDateTime.now();

            addNewYearToTimesheetIfRequired(timesheet, localDateTime);
            addNewWeekToYearIfRequired(timesheet, localDateTime);
            addNewDayToWeekIfRequired(timesheet, localDateTime);
            addNewEntryToDay(timesheet, localDateTime, type);

            timesheetService.write(timesheet);
        } else {
            log.error("Working directory is not ready; application cannot continue.");
        }
    }

    private boolean isWorkingDirectoryReady() {
        final Optional<String> optionalTimesheetLocation = locationService.getTimesheetLocation();
        if (optionalTimesheetLocation.isEmpty()) {
            log.error("Cannot get location of time sheet.");

            return false;
        }

        final String timesheetLocation = optionalTimesheetLocation.get();
        log.info("Location of time sheet is '{}'.", timesheetLocation);
        try {
            log.info("Checking if there is already a time sheet.");
            final File file = new File(timesheetLocation);
            if (!file.exists()) {
                log.error("Time sheet does not exist; creating a new one.");
                return file.createNewFile();
            }

            return true;
        } catch (final IOException e) {
            log.error("Cannot create time sheet.");
        }

        return false;
    }

    private void addNewYearToTimesheetIfRequired(final Timesheet timesheet,
                                                 final LocalDateTime localDateTime) {
        log.info("If required, adding new Year to time sheet.");
        final Optional<Year> optionalLatestYear = timesheet.getLatestYear();
        if (optionalLatestYear.isPresent()) {
            final Year latestYear = optionalLatestYear.get();
            final int latestYearValue = latestYear.getValue();
            final int currentYearValue = localDateTime.getYear();

            if (latestYearValue != currentYearValue) {
                final Year newYear = new Year(localDateTime, currentYearValue);
                timesheet.addYear(newYear);
            }
        } else {
            final int currentYearValue = localDateTime.getYear();
            final Year newYear = new Year(localDateTime, currentYearValue);
            timesheet.addYear(newYear);
        }
    }

    private void addNewWeekToYearIfRequired(final Timesheet timesheet,
                                            final LocalDateTime localDateTime) {
        log.info("If required, adding new Week to time sheet.");
        final Optional<Year> optionalLatestYear = timesheet.getLatestYear();
        if (optionalLatestYear.isPresent()) {
            final Year latestYear = optionalLatestYear.get();
            final Optional<Week> optionalLatestWeek = timesheet.getLatestWeek();
            if (optionalLatestWeek.isPresent()) {
                final Week latestWeek = optionalLatestWeek.get();
                final int latestWeekNumber = latestWeek.getWeekNumber();

                final WeekFields weekFields = WeekFields.of(Locale.getDefault());
                final int nowWeekNumber = localDateTime.get(weekFields.weekOfWeekBasedYear());

                if (latestWeekNumber != nowWeekNumber) {
                    final Week newWeek = new Week(localDateTime);
                    latestYear.addWeek(newWeek);
                }
            } else {
                final Week newWeek = new Week(localDateTime);
                latestYear.addWeek(newWeek);
            }
        }
    }

    private void addNewDayToWeekIfRequired(final Timesheet timesheet,
                                           final LocalDateTime localDateTime) {
        log.info("If required, adding new Day to time sheet.");
        final Optional<Week> optionalLatestWeek = timesheet.getLatestWeek();
        if (optionalLatestWeek.isPresent()) {
            final Week latestWeek = optionalLatestWeek.get();
            final Optional<Day> optionalLatestDay = timesheet.getLatestDay();
            if (optionalLatestDay.isPresent()) {
                final Day latestDay = optionalLatestDay.get();
                final DayOfWeek latestDayOfWeek = latestDay.getDay();

                final DayOfWeek nowDayOfWeek = localDateTime.getDayOfWeek();

                if (!nowDayOfWeek.equals(latestDayOfWeek)) {
                    final Day newDay = new Day(localDateTime);
                    latestWeek.addDay(newDay);
                }
            } else {
                final Day newDay = new Day(localDateTime);
                latestWeek.addDay(newDay);
            }
        }
    }

    private void addNewEntryToDay(final Timesheet timesheet,
                                  final LocalDateTime localDateTime,
                                  final Action.Type type) {
        log.info("Adding new Action to time sheet.");
        final Optional<Day> optionalLatestDay = timesheet.getLatestDay();
        if (optionalLatestDay.isPresent()) {
            final Day latestDay = optionalLatestDay.get();

            final Action action = new Action(type, localDateTime);
            latestDay.addEntry(action);
        }
    }
}
