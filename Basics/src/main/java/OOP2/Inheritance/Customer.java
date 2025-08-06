package OOP2.Inheritance;

public class Customer extends User {
    Customer(String username, String email) {
        super(username, email);
    }

    void browseProducts() {
        System.out.println("Customer is browsing products.🥳");
    }

    @Override
    void showDashboard() {
        System.out.println("Customer's personalized dashboard.");
    }
}
