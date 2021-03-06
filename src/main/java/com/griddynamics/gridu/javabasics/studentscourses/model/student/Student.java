package com.griddynamics.gridu.javabasics.studentscourses.model.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    String name;
    String surname;
    Curriculum curriculum;

    public String getFullName() {
        return name + " " + surname;
    }
}
