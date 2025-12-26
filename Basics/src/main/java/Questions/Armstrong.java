package Questions;

public class Armstrong {
    public static void main(String[] args) {
        System.out.println(isArmstrong(1634));
        printArmstrong(100, 500);
    }

    public static void printArmstrong(int low, int high) {
        for (int n = low; n <= high; n++) {
            boolean res = isArmstrong(n);
            if (res) {
                System.out.println(n);
            }
        }
    }

    public static boolean isArmstrong(int n) {
        int count = countDigits(n); //4
        int originalNum = n; //1634
        int sum = 0;
        while (n > 0) {
            int rem = n % 10;
            sum += (int) Math.pow(rem, count);
            n = n / 10;
        }
        return sum == originalNum;
    }

    public static int countDigits(int n) {
        int count = 0;
        while (n > 0) {
            n = n / 10;
            count++;
        }
        return count;
    }
}
