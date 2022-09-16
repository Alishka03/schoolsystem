package com.example.schoolsystem.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class MailSender {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    public MailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(String emailto, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
            mailMessage.setFrom(username);
            mailMessage.setTo(emailto);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
