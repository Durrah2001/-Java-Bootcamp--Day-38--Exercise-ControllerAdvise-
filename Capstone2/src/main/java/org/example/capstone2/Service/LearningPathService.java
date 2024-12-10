package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.Course;
import org.example.capstone2.Model.LearningPath;
import org.example.capstone2.Model.Session;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.CourseRepository;
import org.example.capstone2.Repository.LearningPathRepository;
import org.example.capstone2.Repository.SessionRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
@RequiredArgsConstructor
@Service
public class LearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;


    public List<LearningPath> getAllLearningPath() {
        return learningPathRepository.findAll();
    }

    public void createLearningPath(LearningPath learningPath) {

        User user = userRepository.findUserByUserId(learningPath.getUserId());
        if (user == null) {
            throw new ApiException("User not found!");
        }

        LearningPath existingLearningPath = learningPathRepository.findLearningPathByLearningPathId(learningPath.getUserId());
        if (existingLearningPath != null) {
            throw new ApiException("Learning path already exists for this user!");
        }


        learningPathRepository.save(learningPath);


    }

    public void updateLearningPath(Integer id, LearningPath learningPath) {

        LearningPath existingLearningPath = learningPathRepository.findLearningPathByLearningPathId(id);

        if (existingLearningPath == null) {
            throw new ApiException("This learning path not found!");
        }

        User user = userRepository.findUserByUserId(learningPath.getUserId());
        if (user == null) {
            throw new ApiException("User not found!");
        }
        existingLearningPath.setCompletedSessions(learningPath.getCompletedSessions());
        existingLearningPath.setCompletedCourses(learningPath.getCompletedCourses());
        existingLearningPath.setStatus(learningPath.getStatus());
        existingLearningPath.setProgressPercentage(learningPath.getProgressPercentage());
        existingLearningPath.setLastInteractiveDate(learningPath.getLastInteractiveDate());

        learningPathRepository.save(existingLearningPath);

    }

    public void deleteLearningPath(Integer id) {

        LearningPath existingLearningPath = learningPathRepository.findLearningPathByLearningPathId(id);

        if (existingLearningPath == null) {
            throw new ApiException("This learning path not found!");
        }

        learningPathRepository.delete(existingLearningPath);
    }

    //End CRUD

    //-- Upgrade th user level based on LearningPath--

    public void upgradeUser(Integer userId) {

        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new ApiException("User not found!");
        }

        //Learning path for a user
        LearningPath learningPath = learningPathRepository.findByUserId(userId);
        if (learningPath == null) {
            throw new ApiException("Learning Path not found for this user!");
        }

        Integer completedSessions = learningPath.getCompletedSessions();
        Integer completedCourses = learningPath.getCompletedCourses();

        //  criteria based on user level
        String currentLevel = user.getExperienceLevel();

        if (currentLevel.equalsIgnoreCase("Beginner")) {
            if (completedSessions >= 5 && completedCourses >= 3) {
                user.setExperienceLevel("Intermediate");
            } else {
                throw new ApiException("User does not meet the criteria for upgrading to Intermediate!");
            }

        } else if (currentLevel.equalsIgnoreCase("Intermediate")) {
            if (completedSessions >= 10 && completedCourses >= 5) {
                user.setExperienceLevel("Advanced");
            } else {
                throw new ApiException("User does not meet the criteria for upgrading to Advanced!");
            }

        } else {
            throw new ApiException("User is already at the highest experience level!");
        }


        userRepository.save(user);
    }


    //-- Notify user about inactivity--

    public String notifyInactivity(Integer userId) {
        //  lp for the user
        LearningPath learningPath = learningPathRepository.findByUserId(userId);

        if (learningPath == null) {
            throw new ApiException("Learning Path not found for this user!");
        }

        // if user in-active for more than 7 days
        LocalDate lastInteractiveDate = learningPath.getLastInteractiveDate();
        LocalDate today = LocalDate.now();

        int daysDiff = Math.toIntExact(ChronoUnit.DAYS.between(lastInteractiveDate, today));
        //will minus (lastInteractive date) ex: 4-12 - (today) ex: 7-12 = 3 days, so no need to notify

        if (daysDiff < 7) { //must be more than 7 days to notify
            return "This user has been active within the last 7 days. No inactivity email is required!";
        }


        User user = userRepository.findUserByUserId(userId);
        if (user == null)
            throw new ApiException("User not found!");

        // email notificatn
        String subject = "We Miss You on Your Learning Path!";
        String body = "Dear " + user.getName() + ",\n\n" +
                "We noticed that you haven’t made progress on your learning path for over 7 days. " +
                "Let’s get back on track and achieve your learning goals!\n\n" +
                "Best regards,\n";


        emailService.sendEmail(user.getEmail(), subject, body);


        return "Email sent successfully to: " + user.getName();
    }

    //-- if course becomes unavailable for any reason (ex: by expert) and already completed by user!
    // redistribute its progress percentage to the remaining courses and sessions in the learning path

    public String redistributeProgress(Integer userId) {
        LearningPath learningPath = learningPathRepository.findByUserId(userId);

        if (learningPath == null)
            throw new ApiException("Learning Path not found for this user!");

        Integer totalProgress = learningPath.getProgressPercentage();

        if (totalProgress == 0) {
            throw new ApiException("No progress to redistribute!");
        }


        int completedCourses = learningPath.getCompletedCourses();
        int completedSessions = learningPath.getCompletedSessions();
        int total = completedCourses + completedSessions;

        if (total == 0) { //nothing complete by user until now -->0
            throw new ApiException("No completed courses or sessions to redistribute progress.");
        }

        // percentage of progress that was to a course
        Integer removedCourseP = totalProgress / total;

        Integer newProgressPercentage = totalProgress - removedCourseP;
        learningPath.setProgressPercentage(newProgressPercentage); //save


        learningPath.setProgressPercentage(totalProgress - removedCourseP);

        learningPathRepository.save(learningPath);
        return "Progress successfully redistributed. Removed: " + removedCourseP + "%, new progress: " + newProgressPercentage + "%.";
    }


}
