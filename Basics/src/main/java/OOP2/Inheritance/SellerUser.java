package OOP2.Inheritance;

public class SellerUser extends User {
    SellerUser(String userName, String email) {
        super(userName, email);
    }

    void addProducts() {
        System.out.println("Adding products.");
    }

    void sellProducts() {
        System.out.println("Seller is selling products.");
    }

    @Override
    void showDashboard() {
        System.out.println("Seller's personalized dashboard.");
    }
}
