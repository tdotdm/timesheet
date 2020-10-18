package com.github.tdotdm.timesheet.checkin.core;

public enum Action {
    IN("in"),
    OUT("out");

    private final String value;

    Action(final String value) {
        this.value = value;
    }
}
