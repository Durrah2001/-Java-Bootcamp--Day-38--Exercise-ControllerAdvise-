package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.Course;
import org.example.capstone2.Model.Expert;
import org.example.capstone2.Model.LearningPath;
import org.example.capstone2.Repository.CourseRepository;
import org.example.capstone2.Repository.ExpertRepository;
import org.example.capstone2.Repository.LearningPathRepository;
import org.example.capstone2.Repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService {

    private final ExpertRepository expertRepository;
    private final CourseRepository courseRepository;
    private final LearningPathRepository learningPathRepository;
    private final SessionRepository sessionRepository;



    public List<Expert> getExperts() {
        return expertRepository.findAll();
    }

    public void addExpert(Expert expert) {
        expertRepository.save(expert);
    }

    public void updateExpert(Integer id, Expert expert) {

        Expert e = expertRepository.findExpertByExpertId(id);

        if (e == null)
            throw new ApiException("Expert with this ID not found to update it!");

        e.setName(expert.getName());
        e.setEmail(expert.getEmail());
        e.setPassword(expert.getPassword());
        e.setExpertiseArea(expert.getExpertiseArea());
        e.setExperienceYears(expert.getExperienceYears());
        e.setTotalSessions(expert.getTotalSessions());

        expertRepository.save(e);

    }

    public void deleteExpert(Integer id) {

        Expert e = expertRepository.findExpertByExpertId(id);

        if (e == null)
            throw new ApiException("Expert with this ID not found to delete it!");

        expertRepository.delete(e);
    }

    //End CRUD

    //-- recommend courses to a learner based on the number of completed sessions--

    public List<Course> recommendCoursesForLearner(Integer learnerId, Integer expertId) {

        LearningPath learningPath = learningPathRepository.findByUserId(learnerId);
        if (learningPath == null)
            throw new ApiException("Learning path not found for learner");

        // check if  learner complete the required number sessions: must more than 5
        if (learningPath.getCompletedSessions() < 5) {
            throw new ApiException("Learner must complete at least 5 sessions before receiving recommendations!");
        }

        List<Course> expertCourses = courseRepository.findCourseByExpertId(expertId);

        if (expertCourses.isEmpty()) {
            throw new ApiException("No courses available from this expert!");
        }

        return expertCourses;
    }


}
