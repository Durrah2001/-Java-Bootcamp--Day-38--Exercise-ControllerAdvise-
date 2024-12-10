package org.example.capstone2.Repository;

import org.example.capstone2.Model.Expert;
import org.example.capstone2.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    Session findSessionBySessionId(Integer id);

    @Query("SELECT s FROM Session s JOIN Expert e ON s.expertId = e.expertId " +
            "WHERE e.expertiseArea = :expertiseArea AND s.duration = :duration")
    List<Session> findSessionsByExpertiseAreaAndDuration(String expertiseArea,
                                                         Integer duration);


}
