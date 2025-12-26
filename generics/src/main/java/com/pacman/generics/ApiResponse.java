package com.pacman.generics;

// A generic response wrapper
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    // Constructor
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

