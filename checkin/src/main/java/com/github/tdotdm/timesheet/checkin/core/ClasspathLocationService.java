package com.github.tdotdm.timesheet.checkin.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(value = "com.github.tdotdm.timesheet.classpath-context", havingValue = "true")
public class ClasspathLocationService implements LocationService {
    private final String fileLocation;

    public ClasspathLocationService(final ApplicationProperties applicationProperties) {
        this.fileLocation = applicationProperties.getClasspathFileLocation();
    }

    @Override
    public Optional<String> getTimesheetLocation() {
        return Optional.of(fileLocation);
    }
}
