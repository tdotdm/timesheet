package com.github.tdotdm.timesheet.library;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusService {
    private final LocationService locationService;
    private final TimesheetService timesheetService;

    public void status() {
        final boolean workingDirectoryIsReady = isWorkingDirectoryReady();
        if (workingDirectoryIsReady) {
            log.info("Reading time sheet.");
            timesheetService
                    .read()
                    .getLatestDay()
                    .flatMap(Day::getLatestEntry)
                    .ifPresent(latestEntry -> {
                        final String timestamp = latestEntry.getTimestamp();
                        final Entry.Type type = latestEntry.getType();

                        logStatus(timestamp, type);
                    });
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

    private void logStatus(final String timestamp,
                           final Entry.Type type) {
        log.info("********** TIMESHEET LATEST **********");
        log.info("Timestamp: {}.", timestamp);
        log.info("Entry.Type: {}.", type);
        log.info("**************************************");
    }
}
