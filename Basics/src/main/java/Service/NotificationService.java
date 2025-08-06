package Service;

interface NotificationService {

    void sendNotification(String message);

    void subscribeToTopic(String topic);
}
