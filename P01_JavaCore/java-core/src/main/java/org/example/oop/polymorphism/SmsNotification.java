package org.example.oop.polymorphism;

public class SmsNotification implements Notification {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending SMS notification: " + message);
    }
}
