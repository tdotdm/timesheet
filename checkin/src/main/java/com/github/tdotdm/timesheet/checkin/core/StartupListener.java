package com.github.tdotdm.timesheet.checkin.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    private static final String TIMESHEET_LOCATION = "bin/timesheet.json";

    private final TimesheetService timesheetService;

    @Override
    public void run(final String... args) {
        final boolean workingDirectoryIsReady = isWorkingDirectoryReady();
        if (workingDirectoryIsReady) {
            log.info("Reading Timesheet.");
            final Timesheet timesheet = timesheetService.read();

            final Optional<Record> latestRecord = timesheetService.getLatestRecord(timesheet);
            if (latestRecord.isPresent()) {
                log.info("Checking if new Record is required.");
                final Record record = latestRecord.get();
                final LocalDateTime now = LocalDateTime.now();
                final WeekFields weekFields = WeekFields.of(Locale.UK);
                final int nowWeekNumber = now.get(weekFields.weekOfWeekBasedYear());

                final int recordWeekNumber = record.getWeekNumber();

                if (recordWeekNumber != nowWeekNumber) {
                    log.info("Latest Record is outdated; adding a new Record to Timesheet.");
                    final Record newRecord = new Record(now, nowWeekNumber, now.getDayOfWeek());
                    timesheet.addRecord(newRecord);
                }
            } else {
                log.info("No Records available; adding a new Record to Timesheet.");
                final LocalDateTime now = LocalDateTime.now();
                final WeekFields weekFields = WeekFields.of(Locale.UK);
                final int nowWeekNumber = now.get(weekFields.weekOfWeekBasedYear());

                final Record newRecord = new Record(now, nowWeekNumber, now.getDayOfWeek());
                timesheet.addRecord(newRecord);
            }

            log.info("Updating latest Record with a new Entry.");
            timesheetService
                    .getLatestRecord(timesheet)
                    .ifPresent(record -> {
                        final Entry entry = new Entry(Action.IN, LocalDateTime.now());
                        record.addEntry(entry);

                        log.info("Writing latest Timesheet to '{}'.", TIMESHEET_LOCATION);
                        timesheetService.write(timesheet);
                    });
        } else {
            log.error("Problem encountered whilst reading Timesheet. Application stopping.");
        }
    }

    private boolean isWorkingDirectoryReady() {
        try {
            log.info("Looking for local Timesheet.");
            final File file = new File(TIMESHEET_LOCATION);
            if (!file.exists()) {
                log.error("Cannot find Timesheet; creating a new one instead.");
                return file.createNewFile();
            }

            return true;
        } catch (final IOException e) {
            log.error("Cannot create Timesheet.");
        }

        return false;
    }
}
