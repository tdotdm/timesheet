package com.github.tdotdm.timesheet.library.service;

import java.util.Optional;

interface LocationService {
    Optional<String> getTimesheetLocation();
}