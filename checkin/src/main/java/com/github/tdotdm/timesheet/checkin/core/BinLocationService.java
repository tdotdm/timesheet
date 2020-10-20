package com.github.tdotdm.timesheet.checkin.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnProperty(value = "com.github.tdotdm.timesheet.classpath-context", havingValue = "false")
public class BinLocationService implements LocationService {
    private final String fileLocation;

    public BinLocationService(final ApplicationProperties applicationProperties) {
        this.fileLocation = applicationProperties.getBinFileLocation();
    }

    @Override
    public Optional<String> getTimesheetLocation() {
        try {
            final String userDir = System.getProperty("user.dir");
            return Optional.of(userDir + fileLocation);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}
