package com.github.tdotdm.timesheet.library;

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
public final class TimesheetService {
    private final Gson gson = new GsonBuilder().create();

    private final LocationService locationService;
    private final TimesheetValidator timesheetValidator;

    public boolean write(final Timesheet timesheet) {
        final Optional<String> optionalTimesheetLocation = locationService.getTimesheetLocation();
        if (optionalTimesheetLocation.isEmpty()) {
            log.error("Cannot get Timesheet's location.");
            return false;
        }

        final List<String> errors = timesheetValidator.validate(timesheet);
        if (!errors.isEmpty()) {
            log.error("Invalid Timesheet:'{}'.", errors.toString());
            return false;
        }

        try {
            final String timesheetLocation = optionalTimesheetLocation.get();
            log.info("Writing Timesheet to '{}'.", timesheetLocation);
            final Writer fileWriter = new FileWriter(timesheetLocation);
            gson.toJson(timesheet, fileWriter);
            fileWriter.flush();
            fileWriter.close();
            log.info("Timesheet successfully written.");
            return true;
        } catch (final IOException e) {
            log.error("Error encountered whilst writing.");
        }

        return false;
    }

    public Timesheet read() {
        final Optional<String> optionalTimesheetLocation = locationService.getTimesheetLocation();
        if (optionalTimesheetLocation.isEmpty()) {
            log.error("Cannot read Timesheet's location.");
            return new Timesheet();
        }

        try {
            final String timesheetLocation = optionalTimesheetLocation.get();
            log.info("Reading Timesheet from '{}'.", timesheetLocation);
            final Timesheet timesheet = gson.fromJson(new FileReader(timesheetLocation), Timesheet.class);
            if (timesheet != null) {
                return timesheet;
            }
        } catch (final FileNotFoundException e) {
            //ignore
        }

        log.error("Timesheet either empty or non-existent; no Records will be available.");
        return new Timesheet();
    }

    public Optional<Record> getLatestRecord(final Timesheet timesheet) {
        final List<Record> records = timesheet.getRecords();
        if (records.isEmpty()) {
            return Optional.empty();
        }

        final Record latestRecord = records.get(records.size() - 1);
        return Optional.ofNullable(latestRecord);
    }
}
