package org.example.oop.polymorphism;

public class EmailNotification implements Notification {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending email notification: " + message);
    }

    public boolean validateEmailAddress(String email) {
        return email != null && email.contains("@");
    }
}
