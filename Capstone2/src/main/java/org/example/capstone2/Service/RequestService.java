package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.ApiResponse.ApiException;
import org.example.capstone2.Model.Request;
import org.example.capstone2.Model.Session;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.RequestRepository;
import org.example.capstone2.Repository.SessionRepository;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;



    public List<Request> getRequests() {
        return requestRepository.findAll();
    }

    public void createRequest(Request request) {

        User user = userRepository.findUserByUserId(request.getUserId());
        Session session = sessionRepository.findSessionBySessionId(request.getSessionId());

        if (user == null)
            throw new ApiException("User not found!");

        if (session == null)
            throw new ApiException("Session not found!");

        request.setStatus("Pending");


        request.setRequestId(null);
        requestRepository.save(request);

    }

    public void updateRequest(Integer reqId, Request request) {


        Request req = requestRepository.findRequestByRequestId(reqId);


        if (req == null)
            throw new ApiException("Request not found!");


        // check first if  request is already approved or rejected, must be pending
        if (req.getStatus().equals("Approved") || req.getStatus().equals("Rejected"))
            throw new ApiException("This request has already been approved or rejected, cannot be updated!");


        // Update
        req.setStatus(request.getStatus());

        req.setRequestDate(request.getRequestDate());
        req.setUrl(request.getUrl());

        //  if the request is approved or rejected will update the associated session's status
        Session session = sessionRepository.findSessionBySessionId(request.getSessionId());

        if (session != null) {
            if (request.getStatus().equals("Approved")) {
                session.setStatus("Approved");
            } else if (request.getStatus().equals("Rejected")) {
                session.setStatus("Cancelled");
            }
            if (request.getStatus().equals("Approved") && session.getStatus().equals("Approved")) {
                // b assume session has already taken
                session.setStatus("Completed");
            }
            sessionRepository.save(session);

        }

        requestRepository.save(req);
    }


    public void deleteRequest(Integer reqId) {
        Request req = requestRepository.findRequestByRequestId(reqId);

        if (req == null)
            throw new ApiException("Request not found!");

        requestRepository.delete(req);

    }

    //End CRUD


}
