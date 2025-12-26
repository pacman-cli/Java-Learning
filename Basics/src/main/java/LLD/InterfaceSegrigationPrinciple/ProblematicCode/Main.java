package LLD.InterfaceSegrigationPrinciple.ProblematicCode;


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Admin) {
                user.canApproveProducts();
            }
        }
    }
}
