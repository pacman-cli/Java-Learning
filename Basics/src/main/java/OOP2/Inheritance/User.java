package OOP2.Inheritance;

public class User {
    protected String userName; //because access modifier is protected now this can be used in all the subclasses
    private String email;

    User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    void login() {
        System.out.println(userName + " Logged In.");
    }

    void showDashboard() {
        System.out.println(userName + "'s Dashboard");
    }
}
