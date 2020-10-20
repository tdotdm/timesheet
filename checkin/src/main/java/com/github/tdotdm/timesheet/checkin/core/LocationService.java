package com.github.tdotdm.timesheet.checkin.core;

import java.util.Optional;

public interface LocationService {
    Optional<String> getTimesheetLocation();
}