package com.n11.eventflow.constants;

import java.time.format.DateTimeFormatter;

public class CommonConstants {
    public static final int MORNING_SESSION_DURATION = 180;
    public static final int AFTERNOON_SESSION_DURATION = 240;
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mma");
}
