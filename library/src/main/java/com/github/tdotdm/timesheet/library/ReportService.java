package com.github.tdotdm.timesheet.library;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public final class ReportService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    private final LocationService locationService;
    private final TimesheetService timesheetService;

    public void report() {
        final boolean workingDirectoryIsReady = isWorkingDirectoryReady();
        if (workingDirectoryIsReady) {
            final Timesheet timesheet = timesheetService.read();

            log.info("Calculating time sheet.");
            calculateHoursForDay(timesheet);
            calculateHoursForWeek(timesheet);
            calculateHoursForYear(timesheet);

            timesheetService.write(timesheet);

            logTotalHours(timesheet);
        } else {
            log.error("Working directory is not ready; application cannot continue.");
        }
    }

    private boolean isWorkingDirectoryReady() {
        final Optional<String> optionalTimesheetLocation = locationService.getTimesheetLocation();
        if (!optionalTimesheetLocation.isPresent()) {
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

    private void calculateHoursForDay(final Timesheet timesheet) {
        final Optional<Year> optionalLatestYear = timesheet.getLatestYear();
        if (optionalLatestYear.isPresent()) {
            final Year latestYear = optionalLatestYear.get();
            final List<Week> weeks = latestYear.getWeeks();
            for (Week week : weeks) {
                final List<Day> days = week.getDays();
                for (Day day : days) {
                    final List<Entry> entries = day.getEntries();
                    final int size = entries.size();
                    double totalHoursForDay = 0;
                    nextEntry:
                    for (int x = 1; x < size; x++) {
                        final Entry currentEntry = entries.get(x);
                        if (Entry.Type.IN.equals(currentEntry.getType())) {
                            continue nextEntry;
                        }
                        final Entry previousEntry = entries.get(x - 1);

                        final String currentActionTimestamp = currentEntry.getTimestamp();
                        final String previousActionTimestamp = previousEntry.getTimestamp();

                        final LocalDateTime currentTimestamp = LocalDateTime.parse(currentActionTimestamp, DATE_TIME_FORMATTER);
                        final LocalDateTime previousTimestamp = LocalDateTime.parse(previousActionTimestamp, DATE_TIME_FORMATTER);
                        final long diffInMinutes = Duration.between(previousTimestamp, currentTimestamp).toMinutes();
                        final double diffInHours = (double) diffInMinutes / 60;

                        totalHoursForDay = totalHoursForDay + diffInHours;
                    }
                    day.setTotalHours(totalHoursForDay);
                }
            }
        }
    }

    private void calculateHoursForWeek(final Timesheet timesheet) {
        final Optional<Year> optionalLatestYear = timesheet.getLatestYear();
        if (optionalLatestYear.isPresent()) {
            final Year latestYear = optionalLatestYear.get();
            final List<Week> weeks = latestYear.getWeeks();
            for (Week week : weeks) {
                final List<Day> days = week.getDays();
                double totalHoursForWeek = 0;
                for (Day day : days) {
                    final double totalHoursForDay = day.getTotalHours();
                    totalHoursForWeek = totalHoursForWeek + totalHoursForDay;
                }
                week.setTotalHours(totalHoursForWeek);
            }
        }
    }

    private void calculateHoursForYear(final Timesheet timesheet) {
        final List<Year> years = timesheet.getYears();
        for (Year year : years) {
            final List<Week> weeks = year.getWeeks();
            double totalHoursForYear = 0;
            for (Week week : weeks) {
                final double totalHoursForWeek = week.getTotalHours();
                totalHoursForYear = totalHoursForYear + totalHoursForWeek;
            }
            year.setTotalHours(totalHoursForYear);
        }
    }

    @SuppressWarnings("MultipleStringLiterals")
    private void logTotalHours(final Timesheet timesheet) {
        log.info("********** TIMESHEET REPORT **********");
        timesheet
                .getLatestYear()
                .ifPresent(latestYear -> {
                    final int year = latestYear.getValue();
                    log.info("Year: {}. Hours: {}.", year, DECIMAL_FORMAT.format(latestYear.getTotalHours()));
                });
        timesheet
                .getLatestWeek()
                .ifPresent(latestWeek -> {
                    final int weekNumber = latestWeek.getWeekNumber();
                    log.info("Week Number: {}. Hours: {}.", weekNumber, DECIMAL_FORMAT.format(latestWeek.getTotalHours()));
                });
        timesheet
                .getLatestDay()
                .ifPresent(latestDay -> {
                    final String day = latestDay.getDay().getDisplayName(TextStyle.FULL, Locale.UK);
                    log.info("Day: {}. Hours: {}.", day, DECIMAL_FORMAT.format(latestDay.getTotalHours()));
                });
        log.info("**************************************");
    }
}
