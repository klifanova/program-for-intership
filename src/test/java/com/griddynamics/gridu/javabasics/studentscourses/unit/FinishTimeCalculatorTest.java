package com.griddynamics.gridu.javabasics.studentscourses.unit;

import com.griddynamics.gridu.javabasics.studentscourses.FinishTimeCalculator;
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

    private FinishTimeCalculator findingEndingTimeForStudent;

    @BeforeEach
    public void init() {
        findingEndingTimeForStudent = new FinishTimeCalculatorImpl();
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T09:00:00.00Z, 1,  2022-04-01T11:00:00.00Z", // start date:friday
            "2022-04-02T09:00:00.00Z, 8,  2022-04-04T18:00:00.00Z", // start date: saturday
            "2022-04-03T18:00:00.00Z, 9,  2022-04-05T11:00:00.00Z", // start date: sunday
            "2022-04-04T19:00:00.00Z, 39, 2022-04-11T17:00:00.00Z", // start date: monday
            "2022-04-03T09:00:00.00Z, 40, 2022-04-08T18:00:00.00Z", // start date: sunday
            "2022-04-04T10:00:00.00Z, 41, 2022-04-11T11:00:00.00Z,",// start date: monday
            "2022-04-04T10:00:00.00Z, 40, 2022-04-08T18:00:00.00Z", // start date: monday
            "2022-04-01T17:00:00.00Z, 1, 2022-04-01T18:00:00.00Z",  // start date: friday
            "2022-04-01T17:00:00.00Z, 2, 2022-04-04T11:00:00.00Z",  // start date: friday
            "2022-04-07T13:00:00.00Z, 8, 2022-04-08T13:00:00.00Z",  // start date: thursday
            "2022-04-04T10:00:00.00Z, 8, 2022-04-04T18:00:00.00Z",  // start date: monday
            "2022-04-04T10:00:00.00Z, 160, 2022-04-29T18:00:00.00Z",// start date: monday
            "2022-04-02T09:00:00.00Z, 73, 2022-04-15T11:00:00.00Z", // start date: saturday
            "2022-04-04T10:00:00.00Z, 80, 2022-04-15T18:00:00.00Z", // start date: monday
            "2022-04-01T10:00:00.00Z, 88, 2022-04-15T18:00:00.00Z", // start date: monday
    })
    public void checkEndDateIfWeCalculateCorrectParameters(String startTime, int durationOfHours,
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
            "2022-04-02T10:00:00.00Z , -1"
    })
    public void checkEndDateIfWeHaveNoValidDuration(String startTime, int durationOfHours) {
        Instant startStampTime = Instant.parse(startTime);
        Duration duration = Duration.of(durationOfHours, ChronoUnit.HOURS);

        assertThrows(InvalidDurationException.class, () -> {
            findingEndingTimeForStudent.calculateFinishTime(startStampTime, duration);
        });
    }
}
