package com.github.tdotdm.timesheet.library;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(value = "com.github.tdotdm.timesheet.classpath-context", havingValue = "true")
final class ClasspathLocationService extends LocationServiceImpl {
    private final String fileLocation;

    ClasspathLocationService(final ApplicationProperties applicationProperties) {
        this.fileLocation = applicationProperties.getClasspathFileLocation();
    }

    @Override
    public Optional<String> getTimesheetLocation() {
        return Optional.of(fileLocation);
    }
}
