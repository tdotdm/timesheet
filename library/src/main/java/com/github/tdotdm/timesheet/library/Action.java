package com.github.tdotdm.timesheet.library;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public final class Action {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private UUID id;
    private String timestamp;
    private Action.Type type;

    public Action(final Action.Type action,
                  final LocalDateTime timestamp) {
        this.id = UUID.randomUUID();
        this.timestamp = timestamp.format(DATE_TIME_FORMATTER);
        this.type = action;
    }

    public enum Type {
        IN("in"),
        OUT("out");

        private final String value;

        Type(final String value) {
            this.value = value;
        }
    }
}
