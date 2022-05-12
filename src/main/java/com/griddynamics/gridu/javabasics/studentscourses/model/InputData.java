package com.griddynamics.gridu.javabasics.studentscourses.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Model for input data

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData {

    private String nameFile;
    private String time;
    private OutputDataType outputDataType;
}