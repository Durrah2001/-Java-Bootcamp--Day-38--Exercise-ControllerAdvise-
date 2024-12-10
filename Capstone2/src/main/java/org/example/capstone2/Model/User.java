package org.example.capstone2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
//@Check(constraints = "experienceLevel= 'Beginner' or experienceLevel= 'Intermediate' or experienceLevel= 'Advanced'")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotEmpty(message = "Name is required!")
    @Column(columnDefinition = "varchar(15) not null")
    private String name;

    @Email(message = "Email should be valid!")
    @NotEmpty(message = "Email is required")
    @Column(columnDefinition = "varchar(30) not null")
    private String email;

    @NotEmpty(message = "Password is required!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    @Column(columnDefinition = "varchar(30) not null")
    private String password;

    @NotEmpty(message = "Experience Level is required!")
    @Pattern(regexp = "^(Beginner|Intermediate|Advanced)$", message = "Experience Level must be either \"Beginner\", \"Intermediate\", or \"Advanced\" only.")
    @Column(columnDefinition = "varchar(13) not null")
    private String experienceLevel;

    @Size(max = 500, message = "Bio should not exceed 500 characters")
    @Column(columnDefinition = "varchar(500)")
    private String bio;

    @Min(value = 0, message = "Points cannot be negative")
    @Column(columnDefinition = "int default 0")
    private Integer points;


    @ElementCollection  //to maps it to a separate table in the database
    @CollectionTable(name = "user_bookmarked_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")

    private List<Integer> bookmarkedCourses = new ArrayList<>();

///////////////////////////

}
