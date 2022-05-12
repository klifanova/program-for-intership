package com.griddynamics.gridu.javabasics.studentscourses.console;

import com.griddynamics.gridu.javabasics.studentscourses.model.InputData;
import com.griddynamics.gridu.javabasics.studentscourses.model.OutputDataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//read input data from console;
//input data consists of fileName,time, outputDaraType;

public class ConsoleData {

    public InputData read() throws IOException {
        InputData inputData = new InputData();
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String line = bufferRead.readLine();
        String[] arrayLines = line.split(", ");
        inputData.setNameFile(arrayLines[0]);
        inputData.setTime(arrayLines[1]);
        inputData.setOutputDataType(OutputDataType.valueOf(arrayLines[2]));
        return inputData;
    }
}
