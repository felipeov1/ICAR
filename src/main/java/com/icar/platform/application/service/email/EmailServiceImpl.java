package com.icar.platform.application.service.email;

import com.icar.platform.infrastructure.storage.config.StorageProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final StorageProperties storageProperties;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        try {
            String verificationUrl = "http://192.168.3.8:8080" + "/api/v1/auth/verify-email?token=" + verificationToken;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Confirme seu e-mail - iCar");

            String htmlContent = buildEmailHtmlContent(verificationUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar e-mail de verificação", e);
        }
    }

    private String buildEmailHtmlContent(String verificationUrl) {
        return "<!DOCTYPE html>" +
                "<html lang=\"pt-BR\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Confirmação de E-mail - iCar</title>" +
                "    <style>" +
                "        body { font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; color: #333333; max-width: 600px; margin: 0 auto; padding: 0; background-color: #f5f5f5; }" +
                "        .container { background-color: #ffffff; margin: 20px auto; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                "        .header { background-color: #2563eb; padding: 30px 20px; text-align: center; }" +
                "        .header h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; }" +
                "        .content { padding: 30px; }" +
                "        h2 { color: #2563eb; margin-top: 0; font-size: 22px; }" +
                "        p { margin-bottom: 16px; font-size: 16px; line-height: 1.5; }" +
                "        .button-container { text-align: center; margin: 30px 0 15px 0; }" +
                "        .button { display: inline-block; padding: 14px 28px; background-color: #2563eb; color: white; text: white; text-decoration: none; border-radius: 6px; font-weight: 600; font-size: 16px; transition: background-color 0.3s; }" +
                "        .button:hover { background-color: #1d4ed8; }" +
                "        .link-container { text-align: center; margin-bottom: 30px; }" +
                "        .link-alternative { display: inline-block; word-break: break-all; color: #6b7280; font-size: 14px; text-decoration: none; border: 1px solid #e5e7eb; padding: 10px 15px; border-radius: 4px; background-color: #f9fafb; max-width: 80%; }" +
                "        .footer { padding: 20px; text-align: center; font-size: 12px; color: #6b7280; border-top: 1px solid #e5e7eb; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>iCar</h1>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <h2>Confirme seu endereço de e-mail</h2>" +
                "            <p>Olá,</p>" +
                "            <p>Obrigado por se cadastrar na iCar. Para ativar sua conta, por favor confirme seu endereço de e-mail clicando no botão abaixo:</p>" +
                "            <div class=\"button-container\">" +
                "                <a href=\"" + verificationUrl + "\" class=\"button\">CONFIRMAR E-MAIL</a>" +
                "            </div>" +
                "            <div class=\"link-container\">" +
                "                <span style=\"color: #6b7280; font-size: 14px; display: block; margin-bottom: 8px;\">Ou copie e cole este link:</span>" +
                "                <a href=\"" + verificationUrl + "\" class=\"link-alternative\">" + verificationUrl + "</a>" +
                "            </div>" +
                "            <p>Atenciosamente,<br><strong>Equipe iCar</strong></p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>© " + LocalDate.now().getYear() + " iCar. Todos os direitos reservados.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}