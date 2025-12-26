package com.pacman.LeapYearChecker;
//Problem#1: Identify Leap Years- Using Ternary Conditional Operator in case statement
public class LeapYearChecker {
    public static String checkYear(Number year) {
        return switch (year){
            case Integer y-> (y % 4==0 && y%100!=0) ||(y%400==0)?"Leap Year.": "Common Year.";
            default -> "Invalid Year.";
        };
    }
    public static void main(String[] args) {
        System.out.println(checkYear(20000));
    }
}
