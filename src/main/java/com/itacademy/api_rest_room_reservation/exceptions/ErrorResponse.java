package com.itacademy.api_rest_room_reservation.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timeStamp;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
