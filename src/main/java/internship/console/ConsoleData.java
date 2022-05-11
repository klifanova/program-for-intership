package internship.console;

import internship.model.InputData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleData {

    public InputData read() throws IOException {
        InputData inputData = new InputData();
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String line = bufferRead.readLine();
        String[] arrayLines = line.split(", ");
        inputData.setNameFile(arrayLines[0]);
        inputData.setTime(arrayLines[1]);
        inputData.setOutputDataType(arrayLines[2]);
        return inputData;
    }
}
