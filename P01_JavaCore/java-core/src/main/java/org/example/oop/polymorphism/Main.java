package org.example.oop.polymorphism;

public class Main {
    public static void main(String[] args) {
        Notification n1 = new EmailNotification();
        Notification n2 = new SmsNotification();
        Notification n3 = new PushNotification();

        NotificationService service = new NotificationService();
        service.sendNotification(n1, "Hello, this is an email notification!");
        service.sendNotification(n2, "Hello, this is an SMS notification!");
        service.sendNotification(n3, "Hello, this is a push notification!");

        EmailNotification n4 = (EmailNotification) n1;
        boolean isValid = n4.validateEmailAddress("Phat@gmail.com");
        System.out.println("Is the email address valid? " + isValid);

        Notification notification = new SmsNotification();

        // cannot cast SmsNotification to EmailNotification, will throw ClassCastException
        EmailNotification email = (EmailNotification) notification;
    }
}
