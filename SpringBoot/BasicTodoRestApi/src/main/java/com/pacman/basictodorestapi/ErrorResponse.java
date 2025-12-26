package com.pacman.basictodorestapi;

public class ErrorResponse {
  private String message;
  private int status;

  ErrorResponse(String message, int status) {
    this.message = message;
    this.status = status;
  }

  public String getMessage() { return this.message; }

  public int getStatus() { return this.status; }
}
