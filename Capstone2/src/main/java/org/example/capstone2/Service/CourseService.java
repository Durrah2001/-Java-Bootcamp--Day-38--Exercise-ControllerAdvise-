package org.example.capstone2.Service;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.*;
import org.example.capstone2.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final ExpertRepository expertRepository;

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final LearningPathRepository learningPathRepository;


    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public void addCourse(Course course) {
        Expert expert = expertRepository.findExpertByExpertId(course.getExpertId());


        if (expert == null) {
            throw new ApiException("Can not add a course without expert!");
        }

        courseRepository.save(course);

    }

    public void updateCourse(Integer id, Course course) {

        Course c = courseRepository.findCourseByCourseId(id);

        if (c == null)
            throw new ApiException("Course with this ID not found to update it!");

        Expert expert = expertRepository.findExpertByExpertId(course.getExpertId());

        if (expert == null)
            throw new ApiException("Can not update a course without expert!");

        c.setTitle(course.getTitle());
        c.setDescription(course.getDescription());
        c.setDurationHours(course.getDurationHours());
        c.setRating(course.getRating());
        c.setPrice(course.getPrice());

        courseRepository.save(c);

    }

    public void deleteCourse(Integer id) {

        Course c = courseRepository.findCourseByCourseId(id);

        if (c == null)
            throw new ApiException("Course with this ID not found to delete it!");

        courseRepository.delete(c);

    }

    //End CRUD

    //--award certificate to user who has completed the course--

    @Transactional
    public String awardCertificate(Integer userId, Integer courseId) {


        User user = userRepository.findUserByUserId(userId);
        if (user == null)
            throw new ApiException("User not found");

        Course course = courseRepository.findCourseByCourseId(courseId);
        if (course == null)
            throw new ApiException("Course not found");

        // Find the user's booking for the course
        Booking booking = bookingRepository.findByUserIdAndCourseIdAndStatus(userId, courseId, "Confirmed");
        if (booking == null)
            throw new ApiException("User has not completed the course yet!");

        // if the certificate has already been awarded
        if (booking.getHasCourseCertificate()) {  //true
            return "Certificate has already been awarded!";
        }

        //  flag to true
        booking.setHasCourseCertificate(true);
        bookingRepository.save(booking);


        //Generate a random code
        String certificateCode = "CERT-" + UUID.randomUUID().toString();

        return "Congratulations! your certificate awarded successfully! Certificate code: " + certificateCode;

    }


    //-- Bookmark courses for future as reference--

    public String bookmarkCourse(Integer userId, Integer courseId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        Course course = courseRepository.findCourseByCourseId(courseId);
        if (course == null) {
            throw new ApiException("Course not found");
        }

        // Check if the course is already bookmarked

        if (user.getBookmarkedCourses().contains(courseId)) {
            return "Course is already bookmarked!";
        }

        // Add
        user.getBookmarkedCourses().add(courseId);
        userRepository.save(user);

        return "Course bookmarked successfully!";
    }

    //-- Discount a course price for users their complete at least 5 sessions--

    List<Integer> allowedDiscounts = Arrays.asList(5, 10, 15, 20, 50);

    @Transactional
    public String applyDiscountToUserCourse(Integer userId, Integer courseId, Integer discountPercentage) {

        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new ApiException("User not found!");
        }

        LearningPath learningPath = learningPathRepository.findByUserId(userId);
        if (learningPath == null || learningPath.getCompletedSessions() < 5) {
            throw new ApiException("User has not completed at least 5 sessions!");
        }

        if (!allowedDiscounts.contains(discountPercentage)) {
            throw new ApiException("Invalid discount percentage!");
        }

        Course course = courseRepository.findCourseByCourseId(courseId);
        if (course == null) {
            throw new ApiException("Course not found!");
        }

        //  Apply  disconut
        double originalPrice = course.getPrice();
        double discountedPrice = (originalPrice - (originalPrice * discountPercentage / 100));

        course.setPrice(discountedPrice);
        courseRepository.save(course);


        return "Discount applied successfully! New course price: " + discountedPrice;
    }

    //--User search for courses based on course durationHours, rating, and price --

    public List<Course> searchCourses(Integer minDuration, Integer minRating, Double maxPrice) {
        List<Course> courses = courseRepository.findAll();
        List<Course> filteredCourses = new ArrayList<>();


        for (Course course : courses) {
            Boolean matches = true;

            if (minDuration != null && course.getDurationHours() < minDuration) {
                matches = false;
            }

            if (minRating != null && course.getRating() < minRating) {
                matches = false;
            }

            if (maxPrice != null && course.getPrice() > maxPrice) {

                matches = false;
            }

            if (matches)
                filteredCourses.add(course);

        }

        return filteredCourses;
    }


}
