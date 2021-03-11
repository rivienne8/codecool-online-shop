package com.codecool.shop.util;

import com.codecool.shop.model.Customer;
import com.codecool.shop.model.Order;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail {
    private final String fromEmail = "kondradkowa696@gmail.com";
    private final String password = "Java123$";
    private Order order;

    public void sendEmail(Order order) {
        this.order = order;


        Properties pr = new Properties();
        pr.setProperty("mail.smtp.host", "smtp.gmail.com");
        pr.setProperty("mail.smtp.port", "587");
        pr.setProperty("mail.smtp.auth", "true");
        pr.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(pr, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = createMessage(session);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private Message createMessage(Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(order.getCustomer().getEmail()));
        message.setSubject("Order number: " + order.getId() + " confirmed.");
        message.setText(createTextMessage());
        return message;
    }

    private String createTextMessage() {
        String message = "Dear " + order.getCustomer().getFirstName() + ", \n" +
        "your order with: \n" +
                order.getCart().getItems() + "\n" +
                "total price " + order.getCart().getTotalPriceString();
        return message;
    }
}
