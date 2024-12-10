package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.Booking;
import org.example.capstone2.Model.Review;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.BookingRepository;
import org.example.capstone2.Repository.ReviewRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;



    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public void addReview(Review review) {

        review.setReviewId(null);

        User user = userRepository.findUserByUserId(review.getUserId());
        Booking booking = bookingRepository.findBookingByBookingId(review.getBookingId());


        if (user == null)
            throw new ApiException("User not found!");

        if (booking == null)
            throw new ApiException("Booking not found to give review about it!");


        if (!booking.getStatus().equalsIgnoreCase("Completed")) {
            throw new ApiException("Booking status must be 'Completed' to review it!");
        }
        reviewRepository.save(review);

    }

    public void updateReview(Integer id, Review review) {

        Review r = reviewRepository.findReviewByReviewId(id);

        if (r == null)
            throw new ApiException("Review with this ID not found!");

        User user = userRepository.findUserByUserId(review.getUserId());
        Booking booking = bookingRepository.findBookingByBookingId(review.getBookingId());

        if (user == null)
            throw new ApiException("User not found!");

        if (booking == null)
            throw new ApiException("Booking not found!");

        if (!booking.getStatus().equalsIgnoreCase("Completed")) {
            throw new ApiException("Booking status must be 'Completed' to review it!");
        }

        r.setRating(review.getRating());
        r.setReviewText(review.getReviewText());
        r.setCreatedAt(LocalDate.now());

        reviewRepository.save(r);

    }

    public void deleteReview(Integer id) {


        Review r = reviewRepository.findReviewByReviewId(id);

        if (r == null)
            throw new ApiException("Review with this ID not found!");

        reviewRepository.delete(r);
    }


    //End CRUD


}
