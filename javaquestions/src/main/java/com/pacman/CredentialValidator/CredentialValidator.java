package com.pacman.CredentialValidator;

public class CredentialValidator {

    // Return a String instead of printing inside the method
    public static String validateCredentials(String username, String password) {
        if (username.startsWith("admin_") && password.length() >= 8) {
            return "Valid admin credentials";
        } else if (username.startsWith("user_") && password.length() >= 6) {
            return "Valid user credentials";
        } else {
            return "Invalid credentials";
        }
    }

    public static void main(String[] args) {
        System.out.println(validateCredentials("admin_user", "strongPass"));
        System.out.println(validateCredentials("admin_user", "weak"));
        System.out.println(validateCredentials("user_test", "validPassword"));
    }
}
