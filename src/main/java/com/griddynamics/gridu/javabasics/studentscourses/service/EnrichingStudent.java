package com.griddynamics.gridu.javabasics.studentscourses.service;

import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;

import java.time.Instant;

public interface EnrichingStudent {

    Student enrichStudent(Instant startTime, Instant endTimeOfCurriculum, Student student);
}
