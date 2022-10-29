package com.futuapi.events.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseModel {

    public static Map<String, Object> successResponse(Object object) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", "success");
        response.put("data", object);
        return response;
    }

    public static Map<String, Object> emptyResponse(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", "success");
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> errorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", "error");
        response.put("message", errorMessage);
        return response;
    }
}