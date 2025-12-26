package Questions;

import java.util.Scanner;

public class ternary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int marks = sc.nextInt();
        String result = (marks > 55) ? "Pass" : "Fail";
        System.out.println(result);
    }
}
