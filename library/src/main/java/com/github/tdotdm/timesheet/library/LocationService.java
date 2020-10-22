package com.github.tdotdm.timesheet.library;

import java.util.Optional;

public interface LocationService {
    Optional<String> getTimesheetLocation();
}