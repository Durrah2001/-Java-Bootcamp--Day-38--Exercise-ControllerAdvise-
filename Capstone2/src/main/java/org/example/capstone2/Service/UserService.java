package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.LearningPath;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.LearningPathRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final LearningPathRepository learningPathRepository;




    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(Integer id, User user) {

        User u = userRepository.findUserByUserId(id);

        if (u == null)
            throw new ApiException("User with this ID not found to update it!");

        u.setName(user.getName());
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        u.setExperienceLevel(user.getExperienceLevel());
        u.setBio(user.getBio());
        u.setPoints(user.getPoints());

        userRepository.save(u);
    }

    public void deleteUser(Integer id) {

        User u = userRepository.findUserByUserId(id);


        if (u == null)
            throw new ApiException("User with this ID not found to delete it!");

        userRepository.delete(u);
    }

    //End CRUD

    //-- Award points to user based on progress in LP --

    public String rewardPoints(Integer userId) {

        StringBuilder message = new StringBuilder();


        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new ApiException("User not found!");
        }


        if (user.getPoints() == 0) {
            // award 10 points to new users to encourage them!
            user.setPoints(10);

            userRepository.save(user);
            message.append("Welcome:) You have been awarded 10 points to get you started with your learning journey! ");
        }

        //Ensure LP for user is exist
        LearningPath learningPath = learningPathRepository.findByUserId(userId);
        if (learningPath == null) {
            throw new ApiException("Learning path not found for user with ID: " + userId);
        }

        // user completed 50% of the Learning Path

        if (learningPath.getProgressPercentage() >= 50) {

            user.setPoints(user.getPoints() + 50);
            userRepository.save(user);
            message.append("Great job! You have earned 50 points for completing 50% of your learning path!");
        }

        if (message.isEmpty()) {
            message.append("No rewards available at this time. Keep progressing!");
        }

        return message.toString();

    }


}
