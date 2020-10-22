package com.github.tdotdm.timesheet.library;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TimesheetValidator {
    public List<String> validate(final Timesheet timesheet) {
        log.info("Validating Timesheet...");
        final List<Record> records = timesheet.getRecords();
        final List<String> errors = new ArrayList<>();
        for (Record record : records) {
            final List<Entry> entries = record.getEntries();
            final int entriesSize = entries.size();
            if (!entries.isEmpty() && entriesSize > 1) {
                final Entry lastEntry = entries.get(entriesSize - 1);
                final Entry secondLastEntry = entries.get(entriesSize - 2);

                if (lastEntry.getAction().equals(secondLastEntry.getAction())) {
                    errors.add("Cannot have sequential Actions.");
                }
            }
        }
        return errors;
    }
}