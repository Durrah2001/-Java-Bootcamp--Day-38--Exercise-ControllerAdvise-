package org.example.capstone2.Repository;

import org.example.capstone2.Model.LearningPath;
import org.example.capstone2.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Integer> {


    LearningPath findLearningPathByLearningPathId(Integer id);

    // ( each user has only one learning path)
    @Query("SELECT lp FROM LearningPath lp WHERE lp.userId = ?1")
    LearningPath findByUserId(Integer userId);



}
