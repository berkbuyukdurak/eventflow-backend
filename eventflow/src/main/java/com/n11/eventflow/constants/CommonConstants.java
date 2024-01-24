package com.n11.eventflow.constants;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CommonConstants {
    // Session durations
    public static final int MORNING_SESSION_DURATION = 180;
    public static final int AFTERNOON_SESSION_DURATION = 240;
    public static final int MAX_SESSION_DURATION = 240;

    // Session start times
    public static final LocalTime MORNING_SESSION_START_TIME = LocalTime.of(9, 0);
    public static final LocalTime AFTERNOON_SESSION_START_TIME = LocalTime.of(13, 0);
    public static final LocalTime LUNCH_TIME = LocalTime.of(12, 0);

    // Networking event timing
    public static final LocalTime NETWORKING_EVENT_START_TIME = LocalTime.of(16, 0);
    public static final LocalTime NETWORKING_EVENT_END_TIME = LocalTime.of(17, 0);

    // Time format
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mma");
}
