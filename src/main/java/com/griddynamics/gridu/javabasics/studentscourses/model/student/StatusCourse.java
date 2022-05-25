package com.griddynamics.gridu.javabasics.studentscourses.model.student;

public enum StatusCourse {
    IN_PROCESS("Training is not finished."),
    COMPLETED("Training completed."),
    NO_COURSE("Training is not have ");

    private final String status;

    StatusCourse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
