package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiResponse;
import org.example.capstone2.Model.Review;
import org.example.capstone2.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/MyKnowledge-YourGrowth/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/get")
    public ResponseEntity getReviews() {
        return ResponseEntity.status(200).body(reviewService.getReviews());
    }

    @PostMapping("/add")
    public ResponseEntity addReview(@RequestBody @Valid Review review) {

        reviewService.addReview(review);

        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateReview(@PathVariable Integer id, @RequestBody @Valid Review review) {


        reviewService.updateReview(id, review);

        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully!"));


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReview(@PathVariable Integer id) {

        reviewService.deleteReview(id);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully!"));
    }

    //End CRUD


}
