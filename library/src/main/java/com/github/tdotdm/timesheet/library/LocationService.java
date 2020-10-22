package com.github.tdotdm.timesheet.library;

import java.util.Optional;

interface LocationService {
    Optional<String> getTimesheetLocation();
}