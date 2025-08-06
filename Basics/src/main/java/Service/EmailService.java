package Service;

public class EmailService implements NotificationService {
    private String email;

    EmailService(String e) {
        this.email = e;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Sending email notifications to :" + email);
    }

    @Override
    public void subscribeToTopic(String topic) {
        System.out.println("Subscribed '" + email + "' to email notifications for topic :" + topic);
    }
}
