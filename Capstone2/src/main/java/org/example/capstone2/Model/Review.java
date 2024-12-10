package org.example.capstone2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @Column(columnDefinition = "int")
    private Integer userId; // Refers to User's ID

    @Column(columnDefinition = "int")
    private Integer bookingId; // Refers to booking's ID //review booking which is a course booked


    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(columnDefinition = "int")
    private Integer rating;

    @Size(max = 500, message = "Review text should not exceed 500 characters!")
    @Column(columnDefinition = "varchar(500)")
    private String reviewText;

    @JsonFormat
    @Column(columnDefinition = "DATE")
    private LocalDate createdAt;


}
