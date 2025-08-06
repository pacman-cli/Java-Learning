package Questions;

import java.util.ArrayList;

public class Adding2Arrays {
    public static void main(String[] args) {
        int[] one = {9, 9, 5, 8};
        int[] two = {6, 7};
        System.out.println(sumOfTwoArrays(one, two));
    }

    public static ArrayList<Integer> sumOfTwoArrays(int[] arr1, int[] arr2) {
        ArrayList<Integer> ans = new ArrayList<>();
        int carry = 0;
        int i = arr1.length - 1;
        int j = arr2.length - 1;
        while (i >= 0 || j >= 0) {
            int sum = 0;

            if (i >= 0) {
                sum += arr1[i];
                i--;
            }
            if (j >= 0) {
                sum += arr2[j];
                j--;
            }

            sum += carry;

            int rem = sum % 10;
            carry = sum / 10;
            ans.add(0, rem);
        }
        if (carry != 0) {
            ans.add(0, carry);
        }
        return ans;
    }
}
