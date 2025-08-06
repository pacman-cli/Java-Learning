package Questions;

public class ArrayQues {
    public static void main(String[] args) {
        int[] arr = {10, 20, 30, 40, 50};
        display(arr);
        reverse(arr);
        display(arr);
//        minNum(arr);
        int k = 3; //rotation factor
        rotate(arr, k);
    }

    public static void rotate(int[] arr, int k) {
        k = k % arr.length;
        if (k < 0) {
            k = k + arr.length;// Convert negative to equivalent positive rotation
        }
        // Perform k times: shift right by 1 each time
        for (int i = 0; i < k; i++) {
//            int last = arr[arr.length - 1]; // store last element
//            for (int j = arr.length - 1; j > 0; j--) {
//                arr[j] = arr[j - 1]; // shift right
//            }
//            arr[0] = last; // put last at first

            //shift left
            int first = arr[0];
            for (int j = 0; j < arr.length - 1; j++) {
                arr[j] = arr[j + 1];
            }
            arr[arr.length - 1] = first;
        }

        System.out.println("Rotated array:");
        for (int value : arr) {
            System.out.print(value + " ");
        }
    }

    private static void minNum(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int a : arr) {
            if (a < min) {
                min = a;
            }
        }
        System.out.print(min);
    }

    public static void display(int[] arr) {
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public static void reverse(int[] arr) {
        int i = 0;
        int j = arr.length - 1;
        while (i <= j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
        }

    }
}
