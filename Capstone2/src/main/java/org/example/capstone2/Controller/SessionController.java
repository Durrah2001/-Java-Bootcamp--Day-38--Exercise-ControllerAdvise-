package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiResponse;
import org.example.capstone2.Model.Session;
import org.example.capstone2.Service.EmailService;
import org.example.capstone2.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/MyKnowledge-YourGrowth/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/get")
    public ResponseEntity getSession() {
        return ResponseEntity.status(200).body(sessionService.getSessions());
    }

    @PostMapping("/add")
    public ResponseEntity addSession(@RequestBody @Valid Session session) {


        sessionService.addSession(session);
        return ResponseEntity.status(200).body(new ApiResponse("Session added successfully!"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateSession(@PathVariable Integer id, @RequestBody @Valid Session session) {

        sessionService.updateSession(id, session);

        return ResponseEntity.status(200).body(new ApiResponse("Session updated successfully!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteSession(@PathVariable Integer id) {

        sessionService.deleteSession(id);
        return ResponseEntity.status(200).body(new ApiResponse("Session deleted successfully!"));

    }

    //End CRUD


    //--Send reminder to user's email before session date--

    @PostMapping("/reminder/{sessionId}")
    public ResponseEntity sendSessionReminder(@PathVariable Integer sessionId,
                                              @RequestParam String userEmail) {

        sessionService.sendReminder(sessionId, userEmail);

        return ResponseEntity.status(200).body(new ApiResponse("Reminder email sent successfully!"));

    }

    //-- Generates a summary report of session for both the user and expert--

    @PostMapping("/generate-summary/{sessionId}")
    public ResponseEntity generateSessionSummary(@PathVariable Integer sessionId) {
        sessionService.generateSessionSummary(sessionId);
        return ResponseEntity.ok(new ApiResponse("Session summary generated and sent successfully!"));
        //Sent now to user and expert email
    }

    //--Notify the learner when expert reschedules a session--

    @PutMapping("/notify-reschedule/{sessionId}")
    public ResponseEntity rescheduleSession(
            @PathVariable Integer sessionId,
            @RequestBody @Valid Session updatedSession) {


        String response = sessionService.rescheduleSession(sessionId, updatedSession);
        return ResponseEntity.status(200).body(new ApiResponse(response));
    }


    //--Search foe sessions by expert expertise area--

    @GetMapping("/search-sessions/{expertiseArea}/{duration}")
    public ResponseEntity searchSessions(@PathVariable String expertiseArea,
                                         @PathVariable Integer duration) {


        List<Session> sessions = sessionService.searchSessionsByAreaAndDuration(expertiseArea, duration);
        return ResponseEntity.status(200).body(new ApiResponse(sessions.toString()));
    }


}
