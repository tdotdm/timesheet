package com.github.tdotdm.timesheet.library;

import java.util.Optional;

public abstract class LocationServiceImpl implements LocationService {
    @Override
    public Optional<String> getTimesheetLocation() {
        throw new UnsupportedOperationException("Default behaviour not overwritten.");
    }
}
