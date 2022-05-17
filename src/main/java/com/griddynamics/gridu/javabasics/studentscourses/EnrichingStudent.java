package com.griddynamics.gridu.javabasics.studentscourses;

import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;

import java.time.Instant;

public interface EnrichingStudent {

    Student enrichStudent(Instant startTime, Instant endTime, Student student);
}