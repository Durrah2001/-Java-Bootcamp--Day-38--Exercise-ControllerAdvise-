package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.Course;
import org.example.capstone2.Model.Expert;
import org.example.capstone2.Model.Session;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.CourseRepository;
import org.example.capstone2.Repository.ExpertRepository;
import org.example.capstone2.Repository.SessionRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final CourseRepository courseRepository;

    private final EmailService emailService;




    public List<Session> getSessions() {
        return sessionRepository.findAll();
    }

    public void addSession(Session session) {

        User user = userRepository.findUserByUserId(session.getUserId());
        Expert expert = expertRepository.findExpertByExpertId(session.getExpertId());

        if (user == null) {
            throw new ApiException("Can not add a session without user!");
        }

        if (expert == null) {
            throw new ApiException("Can not add a session without expert!");
        }

        session.setStatus("Pending");


        session.setSessionId(null);

        sessionRepository.save(session);
    }

    public void updateSession(Integer id, Session session) {

        Session s = sessionRepository.findSessionBySessionId(id);
        if (s == null)
            throw new ApiException("session with this ID not found to update it!");

        User user = userRepository.findUserByUserId(session.getUserId());
        Expert expert = expertRepository.findExpertByExpertId(session.getExpertId());

        if (user == null)
            throw new ApiException("Can not update a session without user!");

        if (expert == null)
            throw new ApiException("Can not update a session without expert!");

        s.setSessionDate(session.getSessionDate());
        s.setDuration(session.getDuration());
        s.setStatus(session.getStatus());

        sessionRepository.save(s);


    }

    public void deleteSession(Integer id) {

        Session s = sessionRepository.findSessionBySessionId(id);
        if (s == null)
            throw new ApiException("session with this ID not found to delete it!");

        sessionRepository.delete(s);

    }
    //End CRUD


    //--Send reminder to user's email before session date--

    public void sendReminder(Integer sessionId, String userEmail) {


        Session session = sessionRepository.findSessionBySessionId(sessionId);

        if (session == null)
            throw new ApiException("Session not found!");

        if(session.getStatus().equalsIgnoreCase("Completed") || session.getStatus().equalsIgnoreCase("Cancelled") ){

            throw new ApiException("Can't send a reminder!");

        }

        LocalDate sessionDate = session.getSessionDate();

        LocalDate reminderTime = sessionDate.minus(1, ChronoUnit.DAYS);

        if (LocalDate.now().isEqual(reminderTime)) {
            String subject = "Session Reminder!";
            String text = "Dear learner :) \nThis is a friendly reminder for your upcoming session at " + sessionDate + ".";
            emailService.sendEmail(userEmail, subject, text);
        } else {
            throw new ApiException("No reminder to send today for session ID: " + sessionId);
        }

    }


    //--Generates a summary report of session for both the user and expert--

    public void generateSessionSummary(Integer sessionId) {

        Session session = sessionRepository.findSessionBySessionId(sessionId);

        if (session == null)
            throw new ApiException("Session not found.");

        if (!session.getStatus().equalsIgnoreCase("Completed")) {
            throw new ApiException("Session must be completed to generate a summary!");
        }
        User user = userRepository.findUserByUserId(session.getUserId());
        Expert expert = expertRepository.findExpertByExpertId(session.getExpertId());


        String summaryReport = "Session Summary:\n" +
                "Learner: " + user.getName() + "\n" +
                "Expert: " + expert.getName() + "\n" +
                "Date: " + session.getSessionDate() + "\n" +
                "Duration: " + session.getDuration() + " minutes";


        emailService.sendEmail(user.getEmail(), "Session Summary", summaryReport);
        emailService.sendEmail(expert.getEmail(), "Session Summary", summaryReport);

    }

    //--Notify the learner when expert reschedules a session--

    public String rescheduleSession(Integer sessionId, Session updatedSession) {

        Session existingSession = sessionRepository.findSessionBySessionId(sessionId);
        if (existingSession == null)
            throw new RuntimeException("Session not found");

        //(must be approved) seesion
        if (!"Approved".equalsIgnoreCase(existingSession.getStatus())) {
            throw new ApiException("Only sessions with status 'Approved' can be rescheduled!");
        }


        existingSession.setSessionDate(updatedSession.getSessionDate());
        existingSession.setDuration(updatedSession.getDuration());

        sessionRepository.save(existingSession);

        User learner = userRepository.findUserByUserId(existingSession.getUserId());

        if (learner == null) {
            throw new RuntimeException("Learner associated with the session not found");
        }

        String message = "Dear " + learner.getName() + ",\n\n" +
                "Your session with expert " + existingSession.getExpertId() + " has been rescheduled.\n\n" +
                "New Session Details:\n" +
                "Date: " + updatedSession.getSessionDate() + "\n" +
                "Duration: " + updatedSession.getDuration() + " minutes.\n\n" +
                "We apologize for the inconvenience, and look forward to your session!";

        emailService.sendEmail(learner.getEmail(), "--Session Rescheduled--", message);

        return "Session rescheduled and learner notified.";
    }


    //-- Search foe sessions by expert expertise area --
    public List<Session> searchSessionsByAreaAndDuration(String expertiseArea, Integer duration) {
        return sessionRepository.findSessionsByExpertiseAreaAndDuration(expertiseArea, duration);
    }


}












