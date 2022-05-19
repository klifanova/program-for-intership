package com.griddynamics.gridu.javabasics.studentscourses.model.inputdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData {

    String fileName;
    String time;
    ReportDataType retortDataType;
}
