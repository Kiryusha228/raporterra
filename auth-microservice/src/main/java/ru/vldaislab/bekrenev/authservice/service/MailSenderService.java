package ru.vldaislab.bekrenev.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("Регистрация прошла успешно");
        message.setText("Здравствуйте, " + username + "! Вы успешно зарегистрировались в raporterra.");

        mailSender.send(message);
    }

    public void sentLoginEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("Кто-то вошел в ваш аккаунт");
        message.setText("Кто-то вошел в ваш аккаунт, если это не вы, то измените пароль");
    }


}