package com.github.tdotdm.timesheet.checkin.core;

import java.util.Date;
import java.util.UUID;

public final class Record {
    private UUID id;
    private Action action;
    private Date date;

    public static Record newInRecord() {
        final Record record = new Record();
        record.id = UUID.randomUUID();
        record.action = Action.IN;
        record.date = new Date();

        return record;
    }

    public static Record newOutRecord() {
        final Record record = new Record();
        record.id = UUID.randomUUID();
        record.action = Action.OUT;
        record.date = new Date();

        return record;
    }
}
