package com.github.tdotdm.timesheet.status;

import com.github.tdotdm.timesheet.library.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    private final StatusService statusService;

    @Override
    public void run(final String... args) {
        statusService.status();
    }
}
