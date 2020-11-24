package com.github.tdotdm.timesheet.checkin;

import com.github.tdotdm.timesheet.library.Action;
import com.github.tdotdm.timesheet.library.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupListener implements CommandLineRunner {
    private final CheckService checkService;

    @Override
    public void run(final String... args) {
        checkService.check(Action.Type.IN);
    }
}
