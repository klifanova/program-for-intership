package com.griddynamics.gridu.javabasics.studentscourses;

import java.time.Duration;
import java.time.Instant;

public interface FinishTimeCalculator {
    Instant calculateFinishTime(Instant localDateTime, Duration duration);
}