package com.icar.platform.application.service.email;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String userName, String verificationToken);
    void sendPasswordResetEmail(String toEmail, String userName, String resetLink);
}