package org.example.capstone2.Repository;

import org.example.capstone2.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {


    Booking findBookingByBookingId(Integer id);

    @Query("SELECT b FROM Booking b WHERE b.userId = ?1 AND b.courseId = ?2 AND b.status = ?3")
    Booking findByUserIdAndCourseIdAndStatus( Integer userId,
                                                  Integer courseId,
                                              String status);







}
