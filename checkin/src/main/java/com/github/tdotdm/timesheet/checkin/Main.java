package com.github.tdotdm.timesheet.checkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.github.tdotdm.timesheet")
@SuppressWarnings("HideUtilityClassConstructor")
public class Main {
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
