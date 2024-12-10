package org.example.capstone2.Repository;

import org.example.capstone2.Model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {

    Expert findExpertByExpertId(Integer id);

}
