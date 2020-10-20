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
    private final TimesheetService timesheetService;

    @Override
    public void run(final String... args) {
        final boolean workingDirectoryIsReady = isWorkingDirectoryReady();
        if (workingDirectoryIsReady) {
            //Read existing timesheet.
            final Timesheet timesheet = timesheetService.read();

            //Check if new record is required
            final Optional<Record> latestRecord = timesheetService.getLatestRecord(timesheet);
            if (latestRecord.isPresent()) {
                final Record record = latestRecord.get();
                final LocalDateTime now = LocalDateTime.now();
                final WeekFields weekFields = WeekFields.of(Locale.UK);
                final int nowWeekNumber = now.get(weekFields.weekOfWeekBasedYear());

                final int recordWeekNumber = record.getWeekNumber();

                if (recordWeekNumber != nowWeekNumber) {
                    //new record
                    final Record newRecord = new Record(now, nowWeekNumber, now.getDayOfWeek());
                    timesheet.addRecord(newRecord);
                }
            } else {
                final LocalDateTime now = LocalDateTime.now();
                final WeekFields weekFields = WeekFields.of(Locale.UK);
                final int nowWeekNumber = now.get(weekFields.weekOfWeekBasedYear());

                //new record
                final Record newRecord = new Record(now, nowWeekNumber, now.getDayOfWeek());
                timesheet.addRecord(newRecord);
            }

            //Update latest record with new Entry
            final Optional<Record> optionalNewLatestRecord = timesheetService.getLatestRecord(timesheet);
            if (optionalNewLatestRecord.isPresent()) {
                final Entry entry = new Entry(Action.IN, LocalDateTime.now());
                final Record newLatestRecord = optionalNewLatestRecord.get();
                newLatestRecord.addEntry(entry);
            } else {
                throw new IllegalStateException("This should never happen.");
            }

            //Save
            timesheetService.write(timesheet);
        } else {
            log.error("Cannot update timesheet; application stopping.");
        }
    }

    private boolean isWorkingDirectoryReady() {
        try {
            log.info("Searching for timesheet.");
            final File file = new File("bin/timesheet.json");
            if (!file.exists()) {
                log.error("Timesheet not found; creating a new one.");
                return file.createNewFile();
            }

            return true;
        } catch (final IOException e) {
            log.error("Cannot create timesheet.");
        }

        return false;
    }
}
