package Service;

public class NotificationExample {
    public static void main(String[] args) {
        NotificationService emailService = new EmailService("puspopuspo@gmail.com");
        NotificationService smsService = new SMSNotificationService("01234567");

        emailService.sendNotification("This is an Email Notification.");
        smsService.sendNotification("This is an Sms Notification.");
        emailService.subscribeToTopic("Email subscribe topic./News");
        smsService.subscribeToTopic("Sms subscribe topic./Alerts");
    }
}
