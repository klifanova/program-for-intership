package com.griddynamics.gridu.javabasics.studentscourses.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData {

    String fileName;
    String time;
    OutputDataType outputDataType;
}
