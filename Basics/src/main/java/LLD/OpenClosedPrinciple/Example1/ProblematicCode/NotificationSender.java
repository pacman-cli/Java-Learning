package LLD.OpenClosedPrinciple.Example1.ProblematicCode;

import java.util.List;

public class NotificationSender {
    public void sendNotifications(List<NotificationType> notificationType) {
        for (NotificationType type : notificationType) {
            if (type == NotificationType.SMS) {
                type.sendSMSNotification();
            } else if (type == NotificationType.WHATSAPP) {
                type.sendWhatsappNotification();
            } else if (type == NotificationType.EMAIL) {
                type.sendEmailNotification();
            }
        }
    }
}
