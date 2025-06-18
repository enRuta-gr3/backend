package com.uy.enRutaBackend.services;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("enruta.lat@gmail.com"); // debe coincidir con spring.mail.username

            mailSender.send(message);

            System.out.println("✅ Email enviado a: " + to);
        } catch (Exception e) {
            System.out.println("❌ Error al enviar email: " + e.getMessage());
        }
    }
}
