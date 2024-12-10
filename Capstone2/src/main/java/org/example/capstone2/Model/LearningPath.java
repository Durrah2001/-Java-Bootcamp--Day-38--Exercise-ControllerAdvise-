package org.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
//@Check(constraints = "status='In Progress' or status='Completed'")
public class LearningPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer learningPathId;

    @Column(columnDefinition = "int")
    private Integer userId; // Refers to User's ID (Learner)

    @PositiveOrZero(message = "Completed sessions must be 0 or greater!")
    @Column(columnDefinition = "int")
    private Integer completedSessions;

    @PositiveOrZero(message = "Completed courses must be 0 or greater!")
    @Column(columnDefinition = "int")
    private Integer completedCourses;

    @NotEmpty(message = "Path learning status is required!")
    @Pattern(regexp = "^(In Progress|Completed)$", message = "Status must be In Progress or Completed!")
    @Column(columnDefinition = "varchar(13) not null")
    private String status;

    @PositiveOrZero(message = "Progress percentage cannot be negative!")
    @Max(value = 100, message = "Progress percentage cannot exceed 100!")
    @Column(columnDefinition = "int")
    private Integer progressPercentage;

    @Column(columnDefinition = "DATE")
    private LocalDate lastInteractiveDate;


}
