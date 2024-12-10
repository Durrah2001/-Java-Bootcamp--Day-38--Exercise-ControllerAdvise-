package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiResponse;
import org.example.capstone2.Model.LearningPath;
import org.example.capstone2.Model.User;
import org.example.capstone2.Service.LearningPathService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/MyKnowledge-YourGrowth/learning-path")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;



    @GetMapping("/get")
    public ResponseEntity getAllLearningPath() {

        return ResponseEntity.status(200).body(learningPathService.getAllLearningPath());
    }

    @PostMapping("/create")
    public ResponseEntity createLearningPath(@RequestBody @Valid LearningPath learningPath) {

        learningPathService.createLearningPath(learningPath);
        return ResponseEntity.status(200).body(new ApiResponse("Learning path added successfully!"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateLearningPath(@PathVariable Integer id, @RequestBody @Valid LearningPath learningPath) {


        learningPathService.updateLearningPath(id, learningPath);

        return ResponseEntity.status(200).body(new ApiResponse("Learning path updated successfully!"));


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteLearningPath(@PathVariable Integer id) {

        learningPathService.deleteLearningPath(id);
        return ResponseEntity.status(200).body(new ApiResponse("Learning path deleted successfully!"));

    }

    //End CRUD

    //-- Upgrade th user level based on LearningPath--

    @PutMapping("/upgrade-user/{userId}")
    public ResponseEntity upgradeUser(@PathVariable Integer userId) {

        learningPathService.upgradeUser(userId);

        return ResponseEntity.status(200).body(new ApiResponse("User has been successfully upgraded !"));

    }

    //7: Notify user about inactivity

    @PostMapping("/notify-inactive/{userId}")
    public ResponseEntity notifyInactivity(@PathVariable Integer userId) {
        String message = learningPathService.notifyInactivity(userId);
        return ResponseEntity.status(200).body(new ApiResponse(message));
    }

    //-- if course becomes unavailable for any reason (ex: by expert) and already completed by user!
    // redistribute its progress percentage to the remaining courses and sessions in the learning path

    @PutMapping("/redistribute-progress/{userId}")
    public ResponseEntity redistributeProgress(@PathVariable Integer userId) {

        String message = learningPathService.redistributeProgress(userId);
        return ResponseEntity.status(200).body(new ApiResponse(message));

    }


}
