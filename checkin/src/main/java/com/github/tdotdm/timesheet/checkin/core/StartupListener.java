package com.github.tdotdm.timesheet.checkin.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    private final TimesheetService timesheetService;

    @Override
    public void run(final String... args) {
        final boolean workingDirectoryIsReady = workingDirectoryIsReady();
        if (workingDirectoryIsReady) {
            final Timesheet timesheet = timesheetService.read();
            timesheet.addRecord(Record.newInRecord());
            timesheetService.create(timesheet);
        } else {
            log.error("Cannot update timesheet; application stopping.");
        }
    }

    private boolean workingDirectoryIsReady() {
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
