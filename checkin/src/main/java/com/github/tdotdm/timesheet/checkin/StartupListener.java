package com.github.tdotdm.timesheet.checkin;

import com.github.tdotdm.timesheet.library.Action;
import com.github.tdotdm.timesheet.library.ServiceRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    private final ServiceRoute serviceRoute;

    @Override
    public void run(final String... args) {
        serviceRoute.run(Action.IN);
    }
}
