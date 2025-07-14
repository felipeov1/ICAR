package com.icar.platform.application.service.email;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String verificationToken);
}
