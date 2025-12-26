package Service;

public class SMSNotificationService implements NotificationService {
    String phoneNumber;

    SMSNotificationService(String phnNbr) {
        this.phoneNumber = phnNbr;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Sending SMS Notification to: " + phoneNumber);
    }

    @Override
    public void subscribeToTopic(String topic) {
        System.out.println("Subscribed '" + phoneNumber + "' to email notifications for topic :" + topic);
    }
}
