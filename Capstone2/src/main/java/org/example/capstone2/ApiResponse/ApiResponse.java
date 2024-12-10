package org.example.capstone2.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class ApiResponse {
    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
