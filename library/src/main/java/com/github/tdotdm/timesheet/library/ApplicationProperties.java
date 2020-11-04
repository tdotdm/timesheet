package com.github.tdotdm.timesheet.library;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.github.tdotdm.timesheet")
class ApplicationProperties {
    private String classpathFileLocation;
    private String binFileLocation;
}
