package LLD.OpenClosedPrinciple.Example1.BetterCode;

public class EmailNotification implements Notification {
    @Override
    public void send() {
        System.out.println("Email Notification");
    }
}
