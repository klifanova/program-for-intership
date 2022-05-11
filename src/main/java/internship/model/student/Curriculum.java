package internship.model.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {

    String name;
    Instant startTimeCourse;
    Instant endTimeCourse;
    Duration duration;

}
