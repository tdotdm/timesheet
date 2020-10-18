package com.github.tdotdm.timesheet.checkin.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimesheetService {
    private static final String TIMESHEET_LOCATION = "bin/timesheet.json";

    private final Gson gson = new GsonBuilder().create();

    public void create(final Timesheet timesheet) {
        try {
            log.info("Writing timesheet to '{}'.", TIMESHEET_LOCATION);
            final Writer fileWriter = new FileWriter(TIMESHEET_LOCATION);
            gson.toJson(timesheet, fileWriter);
            fileWriter.flush();
            fileWriter.close();
            log.info("Timesheet successfully updated.");
        } catch (final IOException e) {
            //ignore
        }
    }

    public Timesheet read() {
        try {
            log.info("Reading timesheet from '{}'.", TIMESHEET_LOCATION);
            final Timesheet timesheet = gson.fromJson(new FileReader(TIMESHEET_LOCATION), Timesheet.class);
            if (timesheet != null) {
                return timesheet;
            }
        } catch (final FileNotFoundException e) {
            //ignore
        }

        log.error("Timesheet either empty or non-existent; returning a new instance.");
        return new Timesheet();
    }
}
