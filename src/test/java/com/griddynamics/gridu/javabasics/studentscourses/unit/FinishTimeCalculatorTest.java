package com.griddynamics.gridu.javabasics.studentscourses.unit;

import com.griddynamics.gridu.javabasics.studentscourses.FinishTimeCalculatorImpl;
import com.griddynamics.gridu.javabasics.studentscourses.exception.InvalidDurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FinishTimeCalculatorTest {

    private FinishTimeCalculatorImpl findingEndingTimeForStudent;

    @BeforeEach
    public void init() {
        findingEndingTimeForStudent = new FinishTimeCalculatorImpl();
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-03T18:00:00.00Z , 9 , 2022-04-05T11:00:00.00Z",
            "2022-04-04T19:00:00.00Z , 39 , 2022-04-11T17:00:00.00Z",
            "2022-04-02T09:00:00.00Z , 73 , 2022-04-15T11:00:00.00Z",
            "2022-04-01T10:00:00.00Z , 88, 2022-04-18T18:00:00.00Z",
    })
    public void checkValidEndDateIfWeCalculateCorrectParameters(String startTime, int durationOfHours,
                                                                String expectedEndTime) {
        Instant startStampTime = Instant.parse(startTime);
        Duration duration = Duration.of(durationOfHours, ChronoUnit.HOURS);
        Instant expectedEndTimeDate = Instant.parse(expectedEndTime);

        Instant actualEndTimeDate = findingEndingTimeForStudent.calculateFinishTime(startStampTime, duration);

        assertEquals(expectedEndTimeDate, actualEndTimeDate);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-04T10:00:00.00Z , 0",
            "2022-04-02T10:00:00.00Z , -1",
            "2022-04-03T10:00:00.00Z , -99"
    })
    public void checkValidEndDateIfWeHaveDurationNoValid(String startTime, int durationOfHours) {
        Instant startStampTime = Instant.parse(startTime);
        Duration duration = Duration.of(durationOfHours, ChronoUnit.HOURS);

        assertThrows(InvalidDurationException.class, () -> {
            findingEndingTimeForStudent.calculateFinishTime(startStampTime, duration);
        });
    }
}
