package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.Course;
import org.example.capstone2.Model.Booking;
import org.example.capstone2.Model.Session;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.CourseRepository;
import org.example.capstone2.Repository.BookingRepository;
import org.example.capstone2.Repository.SessionRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;



    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public void createBooking(Booking booking) {

        Course course = courseRepository.findCourseByCourseId(booking.getCourseId());
        User user = userRepository.findUserByUserId(booking.getUserId());

        if (user == null)
            throw new ApiException("User not found!");

        if (course == null)
            throw new ApiException("Course not found!");

        booking.setStatus("Pending");

        bookingRepository.save(booking);
    }


    public void updateBooking(Integer id, Booking booking) {

        Booking b = bookingRepository.findBookingByBookingId(id);
        if (b == null)
            throw new ApiException("Booking with this ID not found to update it!");


        Course course = courseRepository.findCourseByCourseId(booking.getCourseId());
        User user = userRepository.findUserByUserId(booking.getUserId());

        if (user == null)
            throw new ApiException("User not found!");


        if (course == null)
            throw new ApiException("Course not found!");


        b.setBookingDate(LocalDate.now());
        b.setStatus(booking.getStatus());


        bookingRepository.save(b);

    }

    public void deleteBooking(Integer id) {

        Booking b = bookingRepository.findBookingByBookingId(id);
        if (b == null)
            throw new ApiException("Booking with this ID not found to delete it!");

        bookingRepository.delete(b);

    }

    ////End CRUD


}
