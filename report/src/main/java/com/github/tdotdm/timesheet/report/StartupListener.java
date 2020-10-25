package com.github.tdotdm.timesheet.report;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    @Override
    public void run(final String... args) {
    }
}
