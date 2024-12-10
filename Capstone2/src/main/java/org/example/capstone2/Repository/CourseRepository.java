package org.example.capstone2.Repository;

import org.example.capstone2.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {


    Course findCourseByCourseId(Integer id);


    // (the courses that the expert created)
    List<Course> findCourseByExpertId(Integer expertId);



}
