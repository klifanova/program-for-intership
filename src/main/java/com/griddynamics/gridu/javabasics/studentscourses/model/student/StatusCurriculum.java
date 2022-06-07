package com.griddynamics.gridu.javabasics.studentscourses.model.student;

public enum StatusCurriculum {
    IN_PROCESS("Training is not finished."),
    COMPLETED("Training completed."),
    NO_COURSE("Training is not have ");

    private final String status;

    StatusCurriculum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
