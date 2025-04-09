package com.icar.plataform.application.service.email;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String verificationToken);
}
