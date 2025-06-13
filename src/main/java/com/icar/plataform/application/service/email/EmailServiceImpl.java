package com.icar.plataform.application.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        try {
            String verificationUrl = baseUrl + "/api/v1/auth/verify-email?token=" + verificationToken;

            String loginUrl = baseUrl + "/entrar";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Confirme seu e-mail - iCar");

            String htmlContent = buildEmailHtmlContent(verificationUrl, loginUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar e-mail de verificação", e);
        }
    }

    private String buildEmailHtmlContent(String verificationUrl, String loginUrl) {
        return "<!DOCTYPE html>" +
                "<html lang=\"pt-BR\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Confirmação de E-mail</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "        .header { background-color: #2563eb; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }" +
                "        .header img { max-width: 150px; }" +
                "        .content { padding: 30px; background-color: #f9fafb; border-radius: 0 0 8px 8px; }" +
                "        .button { display: inline-block; padding: 12px 24px; background-color: #2563eb; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                "        .footer { margin-top: 30px; font-size: 12px; color: #6b7280; text-align: center; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"header\">" +
                "        <h1 style=\"color: white; margin: 0;\">iCar Platform</h1>" +
                "    </div>" +
                "    <div class=\"content\">" +
                "        <h2>Confirme seu endereço de e-mail</h2>" +
                "        <p>Olá,</p>" +
                "        <p>Obrigado por se cadastrar na iCar Platform. Para ativar sua conta, por favor confirme seu endereço de e-mail clicando no botão abaixo:</p>" +
                "        <p style=\"text-align: center; margin: 30px 0;\">" +
                "            <a href=\"" + verificationUrl + "\" class=\"button\">Confirmar E-mail</a>" +
                "        </p>" +
                "        <p>Se você não se cadastrou na iCar Platform, por favor ignore este e-mail.</p>" +
                "        <p>Atenciosamente,<br>Equipe iCar Platform</p>" +
                "    </div>" +
                "    <div class=\"footer\">" +
                "        <p>© " + LocalDate.now().getYear() + " iCar Platform. Todos os direitos reservados.</p>" +
                "        <p>Se o botão não funcionar, copie e cole este link no seu navegador:<br>" +
                "        <a href=\"" + verificationUrl + "\">" + verificationUrl + "</a></p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}