package com.n11.eventflow.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class GenericResponseHandler {
    public static ResponseEntity<Object> successResponse(HttpStatus status, Object data) {
        Map<String, Object> result = new HashMap<>();

        result.put("status", Boolean.TRUE);
        result.put("data", data);
        return new ResponseEntity<>(result, status);
    }

    public static ResponseEntity<Object> errorResponse(HttpStatus status, String errorMessage) {
        Map<String, Object> result = new HashMap<>();

        result.put("status", Boolean.FALSE);
        result.put("errorMessage", errorMessage);
        return new ResponseEntity<>(result, status);
    }
}
