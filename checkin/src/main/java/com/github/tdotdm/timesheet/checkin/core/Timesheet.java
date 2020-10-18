package com.github.tdotdm.timesheet.checkin.core;

import java.util.ArrayList;
import java.util.List;

public class Timesheet {
    private final List<Record> records = new ArrayList<>();

    public void addRecord(final Record record) {
        this.records.add(record);
    }
}
