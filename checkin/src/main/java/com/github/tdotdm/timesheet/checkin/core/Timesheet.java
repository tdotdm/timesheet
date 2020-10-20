package com.github.tdotdm.timesheet.checkin.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Timesheet {
    private final List<Record> records = new ArrayList<>();

    public void addRecord(final Record record) {
        this.records.add(record);
    }
}
