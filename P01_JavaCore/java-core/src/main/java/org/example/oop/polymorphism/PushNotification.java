package org.example.oop.polymorphism;

public class PushNotification implements Notification {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending push notification: " + message);
    }
}
