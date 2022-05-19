package com.griddynamics.gridu.javabasics.studentscourses.model.student;

public enum StatusCourse {
    IN_PROCESS("Training is not finished."),
    COMPLETED("Training completed.");

    private final String status;

    StatusCourse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
