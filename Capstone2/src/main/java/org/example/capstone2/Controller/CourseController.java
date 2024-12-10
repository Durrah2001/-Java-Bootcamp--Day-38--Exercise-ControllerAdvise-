package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiResponse;
import org.example.capstone2.Model.Course;
import org.example.capstone2.Service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/MyKnowledge-YourGrowth/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;



    @GetMapping("/get")
    public ResponseEntity getCourses() {
        return ResponseEntity.status(200).body(courseService.getCourses());
    }

    @PostMapping("/add")
    public ResponseEntity addCourse(@RequestBody @Valid Course course) {


        courseService.addCourse(course);
        return ResponseEntity.status(200).body(new ApiResponse("Course added successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCourse(@PathVariable Integer id, @RequestBody @Valid Course course) {

        courseService.updateCourse(id, course);
        return ResponseEntity.status(200).body(new ApiResponse("Course updated successfully!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCourse(@PathVariable Integer id) {

        courseService.deleteCourse(id);
        return ResponseEntity.status(200).body(new ApiResponse("Course deleted successfully!"));

    }

    //End CRUD

    //--award certificate to user who has completed the course--

    @PutMapping("/award-certificate/{userId}/{courseId}")
    public ResponseEntity awardCertificate(@PathVariable Integer userId, @PathVariable Integer courseId) {

        String message = courseService.awardCertificate(userId, courseId);

        ApiResponse response = new ApiResponse(message);
        return ResponseEntity.status(200).body(response);

    }

    //-- Bookmark courses for future as reference--
    @PostMapping("/bookmark-course")
    public ResponseEntity bookmarkCourse(@RequestParam Integer userId, @RequestParam Integer courseId) {

        String response = courseService.bookmarkCourse(userId, courseId);
        return ResponseEntity.status(200).body(new ApiResponse(response));

    }

    //--Discount a course price for users their complete at least 5 sessions--

    @PutMapping("/apply-discount/{userId}/{courseId}/{discountPercentage}")
    public ResponseEntity<ApiResponse> applyDiscount(@PathVariable Integer userId, @PathVariable Integer courseId, @PathVariable Integer discountPercentage) {

        String result = courseService.applyDiscountToUserCourse(userId, courseId, discountPercentage);
        return ResponseEntity.status(200).body(new ApiResponse(result));

    }

    //--User search for courses based on course durationHours, rating, and price --

    @GetMapping("/search-courses/{minDuration}/{minRating}/{maxPrice}")
    public ResponseEntity searchCourses(@PathVariable Integer minDuration,
                                        @PathVariable Integer minRating,
                                        @PathVariable Double maxPrice) {

        List<Course> courses = courseService.searchCourses(minDuration, minRating, maxPrice);
        return ResponseEntity.status(200).body(new ApiResponse(courses.toString()));
    }


}
