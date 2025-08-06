package LLD.OpenClosedPrinciple.Example1.BetterCode;

public class PushNotification implements Notification {
    @Override
    public void send() {
        System.out.println("Notification is pushed");
    }
}
