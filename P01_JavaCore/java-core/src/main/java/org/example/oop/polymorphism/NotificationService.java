package org.example.oop.polymorphism;

public class NotificationService {
    void sendNotification(Notification notification, String message){
        notification.sendNotification(message);
    }
}
