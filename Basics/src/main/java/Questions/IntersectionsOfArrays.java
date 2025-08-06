package Questions;

import java.util.ArrayList;

public class IntersectionsOfArrays {
    public static void main(String[] args) {
        //Unsorted
//        ArrayList<Integer> arr = new ArrayList<>();
//        int[] arr1 = {1, 2, 3, 4, 5, 6};
//        int[] arr2 = {9, 8, 2, 4, 0, 3};
//        for (int i=0; i<arr1.length; i++) {
//            for (int j=0; j<arr2.length; j++) {
//                if(arr1[i]==arr2[j]){
//                    System.out.println("For arr1 :"+arr1[i] + " Index:" + i + " For arr2 :"+arr1[j] + " Index:" + j);
//                    arr.add(arr1[i]);
//                }
//            }
//        }

        //for sorted and optimized
        int[] one = {10, 20, 30, 40, 50};
        int[] two = {15, 25, 29, 30, 40};
        System.out.println(intersection(one, two));
    }

    public static ArrayList<Integer> intersection(int[] one, int[] two) {
        ArrayList<Integer> ans = new ArrayList<>();
        int i = 0, j = 0;
        while (i < one.length && j < two.length) {
            if (one[i] > two[j]) {
                j++;
            } else if (one[i] < two[j]) {
                i++;
            } else {
                ans.add(one[i]);
                i++;
                j++;
            }
        }
        return ans;
    }
}
