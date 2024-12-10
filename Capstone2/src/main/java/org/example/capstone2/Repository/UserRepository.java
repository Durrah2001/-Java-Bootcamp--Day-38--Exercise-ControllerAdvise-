package org.example.capstone2.Repository;

import org.example.capstone2.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUserId(Integer id);

    // Search learners by keywords in their bio
    List<User> findByBioContainingIgnoreCase(String keyword);


}
