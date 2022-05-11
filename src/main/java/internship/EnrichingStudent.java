package internship;

import internship.model.student.Student;

import java.time.Instant;

public interface EnrichingStudent {

    Student enrichStudent(Instant startTime, Instant endTime, Student student);
}
