package com.github.tdotdm.timesheet.library.service;

import com.github.tdotdm.timesheet.library.domain.Timesheet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportService {
    private final LocationService locationService;
    private final TimesheetService timesheetService;

    public void report() {
        final boolean workingDirectoryIsReady = isWorkingDirectoryReady();
        if (workingDirectoryIsReady) {
            final Timesheet timesheet = timesheetService.read();
            
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
}
