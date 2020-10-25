package com.github.tdotdm.timesheet.library.service;

import com.github.tdotdm.timesheet.library.domain.Timesheet;
import com.github.tdotdm.timesheet.library.validator.TimesheetValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public final class TimesheetService {
    private final Gson gson = new GsonBuilder().create();

    private final LocationService locationService;
    private final TimesheetValidator timesheetValidator;

    boolean write(final Timesheet timesheet) {
        final Optional<String> optionalTimesheetLocation = locationService.getTimesheetLocation();
        if (optionalTimesheetLocation.isEmpty()) {
            log.error("Cannot get location of time sheet.");
            return false;
        }

        final List<String> errors = timesheetValidator.validate(timesheet);
        if (!errors.isEmpty()) {
            log.error("Time sheet is invalid, and has the following errors: '{}'.", errors.toString());
            return false;
        }

        try {
            final String timesheetLocation = optionalTimesheetLocation.get();
            log.info("Time sheet is being written to '{}'.", timesheetLocation);
            final Writer fileWriter = new FileWriter(timesheetLocation);
            gson.toJson(timesheet, fileWriter);
            fileWriter.flush();
            fileWriter.close();
            log.info("Time sheet successfully written.");
            return true;
        } catch (final IOException e) {
            log.error("Error encountered whilst writing time sheet.");
        }

        return false;
    }

    public Timesheet read() {
        final Optional<String> optionalTimesheetLocation = locationService.getTimesheetLocation();
        if (optionalTimesheetLocation.isEmpty()) {
            log.info("Cannot get location of time sheet; using new time sheet.");
            return new Timesheet();
        }

        try {
            final String timesheetLocation = optionalTimesheetLocation.get();
            log.info("Time sheet is being read from '{}'.", timesheetLocation);
            final Timesheet timesheet = gson.fromJson(new FileReader(timesheetLocation), Timesheet.class);
            if (timesheet != null) {
                return timesheet;
            }
        } catch (final FileNotFoundException e) {
            //ignore
        }

        log.info("Time sheet is either empty or non-existent; using a new time sheet to prevent further errors.");
        return new Timesheet();
    }
}
