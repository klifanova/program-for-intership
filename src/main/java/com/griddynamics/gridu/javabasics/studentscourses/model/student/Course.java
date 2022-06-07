package com.griddynamics.gridu.javabasics.studentscourses.model.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    String name;
    Instant startTimeCourse;
    Instant endTimeCourse;
    Duration duration;
}
