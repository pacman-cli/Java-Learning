package LLD.OpenClosedPrinciple.Example1.BetterCode;

public class WhatsAppNotification implements Notification {
    @Override
    public void send() {
        System.out.println("WhatsApp Notification");
    }
}
