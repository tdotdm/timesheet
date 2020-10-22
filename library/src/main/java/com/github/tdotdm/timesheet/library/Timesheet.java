package com.github.tdotdm.timesheet.library;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
final class Timesheet {
    private final List<Record> records = new ArrayList<>();

    void addRecord(final Record record) {
        this.records.add(record);
    }
}
