package com.griddynamics.gridu.javabasics.studentscourses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//Converter Json to special class Java Objects

public class JsonConverter {

    public <T> T converterJson(String fileName, Class<T> clazz) {
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
        Object dataJson = null;

        try {
            dataJson = mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (clazz.isInstance(dataJson)) {
            return clazz.cast(dataJson);
        } else {
            throw new IllegalArgumentException("JSON does not represent an instance of type:" + clazz.getName());
        }
    }
}
