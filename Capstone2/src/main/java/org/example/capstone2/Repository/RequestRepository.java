package org.example.capstone2.Repository;

import org.example.capstone2.Model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    Request findRequestByRequestId(Integer id);

}
