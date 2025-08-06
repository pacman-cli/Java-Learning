package OOP2.Inheritance;

public class MainClient {
    public static void main(String[] args) {
        //Admin
        AdminUser admin = new AdminUser("Rahul", "rahul@gmail.com");
        admin.login();
        //admin.AdminProducts();
        admin.showDashboard();

        //Regular
//        Customer regularUser = new Customer("Rahim", "rahim@gmail.com");
//        regularUser.login();
//        regularUser.browseProducts();

        //Seller
//        SellerUser sellerUser = new SellerUser("Karim", "karim@gmail.com");
//        sellerUser.login();
//        sellerUser.sellProducts();
    }
}
