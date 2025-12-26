package Questions;

public class primeCheck {
    public static void main(String[] args) {
        int n = 7;
        //int div =2;
        int flag = 0;
        for (int i = 2; i <= n - 1; i++) {
            if (n % i == 0) {
                flag = 1;
                break;
            }
        }
//        while(div<=n-1){
//            if(n%div==0){
//                flag=1;
//                break;
//            }
//            div++;
//        }
        if (flag == 1) {
            System.out.println("Not prime");
        } else {
            System.out.println("Prime");
        }
    }
}
