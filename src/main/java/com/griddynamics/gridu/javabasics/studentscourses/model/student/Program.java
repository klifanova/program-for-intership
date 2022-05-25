package com.griddynamics.gridu.javabasics.studentscourses.model.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    Curriculum curriculum;
    StatusCourse statusCourse;
    String leftTime;
}
