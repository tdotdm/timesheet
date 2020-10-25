package com.github.tdotdm.timesheet.report;

import com.github.tdotdm.timesheet.library.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    private final ReportService reportService;

    @Override
    public void run(final String... args) {
        reportService.report();
    }
}
