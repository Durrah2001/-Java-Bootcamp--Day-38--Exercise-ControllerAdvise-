package org.example.capstone2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
//@Check(constraints = "sessionType='One-on-One' or sessionType='Workshop'")
//@Check(constraints = "status='Pending' or status='Completed' or status='Cancelled'")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @Column(columnDefinition = "int")
    private Integer userId; // Refers to User's ID (Learner)

    @Column(columnDefinition = "int")
    private Integer expertId; // Refers to Expert's ID


    @JsonFormat
    @Column(columnDefinition = "DATE")
    private LocalDate sessionDate;

    @Min(value = 15, message = "Duration must be at least 15 minutes!")
    @Column(columnDefinition = "int")
    private Integer duration;

    @NotEmpty(message = "Status is required")
    @Pattern(regexp = "^(Pending|Completed|Cancelled)$", message = "Status must be either Pending, Completed, or Cancelled")
    @Column(columnDefinition = "varchar(13) not null")
    private String status;


    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", expertId=" + expertId +
                ", sessionDate=" + sessionDate +
                ", duration=" + duration +
                ", status='" + status + '\'' +
                '}';
    }
}
