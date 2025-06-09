package com.example.TradeTide.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "TradeTide - Email Verification";
        String content = "<p>Dear User,</p>"
                + "<p>Thank you for registering with TradeTide. Please use the following OTP to verify your email address:</p>"
                + "<h2>" + otp + "</h2>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p>Best regards,<br/>TradeTide Team</p>";
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        mimeMessageHelper.setTo(email);
        try {
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException("Failed to send verification email");
        }
    }
}
