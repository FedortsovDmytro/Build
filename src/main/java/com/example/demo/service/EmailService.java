package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Підтвердження реєстрації";

        String message = "Привіт!\n\n" +
                "Твій код підтвердження: " + verificationCode + "\n\n" +
                "Або перейди за посиланням:\n" +
                "http://localhost:8080/verify?email=" + toEmail;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("fedortsovdmytro@gmail.com");

        mailSender.send(mailMessage);
    }
}