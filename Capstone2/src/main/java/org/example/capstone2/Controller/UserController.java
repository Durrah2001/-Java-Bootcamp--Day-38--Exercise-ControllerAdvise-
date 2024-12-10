package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiResponse;
import org.example.capstone2.Model.User;
import org.example.capstone2.Service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/MyKnowledge-YourGrowth/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/get")
    public ResponseEntity getUsers() {

        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user) {


        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully!"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody @Valid User user) {


        userService.updateUser(id, user);

        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully!"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {

        userService.deleteUser(id);

        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully!"));

    }
    //End CRUD

    //-- Award points to user based on progress in LP--
    @PutMapping("/reward-points/{userId}")
    public ResponseEntity rewardPoints(@PathVariable Integer userId) {


        String result = userService.rewardPoints(userId);

        return ResponseEntity.status(200).body(new ApiResponse(result));

    }


}
