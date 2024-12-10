package org.example.capstone2.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Expert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expertId;

    @NotEmpty(message = "Name is required!")
    private String name;

    @Email(message = "Email should be valid!")
    @NotEmpty(message = "Email is required!")
    @Column(columnDefinition = "varchar(20) not null unique")
    private String email;

    @NotEmpty(message = "Password is required!")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    @Column(columnDefinition = "varchar(30) not null")
    private String password;

    @NotEmpty(message = "Expertise area is required!")
    @Column(columnDefinition = "varchar(15) not null")
    private String expertiseArea;

    @Min(value = 1, message = "Experience years must be at least 1")
    @Column(columnDefinition = "int")
    private Integer experienceYears;

    @Min(value = 0, message = "Total sessions must be 0 or more!")
    @Column(columnDefinition = "int")

    private Integer totalSessions;


}
