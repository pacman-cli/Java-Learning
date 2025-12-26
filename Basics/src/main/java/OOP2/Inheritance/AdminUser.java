package OOP2.Inheritance;

//this extends User (now all the functionality of User will be in the AdminUser)
public class AdminUser extends User {
    AdminUser(String userName, String email) {
        super(userName, email);
    }

    void AdminProducts() {
        System.out.println("Admin is managing products.");
    }

    void updateUerName() {
        System.out.println(userName + "'s username needs to be updated.");
    }

    @Override
    void showDashboard() {
        super.showDashboard();
        System.out.println("Admin's personalized dashboard.");
    }

}
