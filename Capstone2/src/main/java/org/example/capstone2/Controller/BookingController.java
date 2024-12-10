package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiResponse;
import org.example.capstone2.Model.Booking;
import org.example.capstone2.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/MyKnowledge-YourGrowth/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/get")
    public ResponseEntity getBookings() {
        return ResponseEntity.status(200).body(bookingService.getBookings());
    }

    @PostMapping("/create")
    public ResponseEntity createBooking(@RequestBody @Valid Booking booking) {


        bookingService.createBooking(booking);
        return ResponseEntity.status(200).body(new ApiResponse("Booking created successfully!"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateBoking(@PathVariable Integer id, @RequestBody @Valid Booking booking) {

        bookingService.updateBooking(id, booking);
        return ResponseEntity.status(200).body(new ApiResponse("Booking updated successfully!"));


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBooking(@PathVariable Integer id) {

        bookingService.deleteBooking(id);
        return ResponseEntity.status(200).body(new ApiResponse("Booking deleted successfully!"));

    }

    //End CRUD


}
