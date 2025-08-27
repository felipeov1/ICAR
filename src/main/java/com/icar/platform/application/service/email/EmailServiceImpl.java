package com.icar.platform.application.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.api-url}")
    private String apiUrl;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.logo-url}")
    private String logoUrl;

    @Override
    @Async
    public void sendVerificationEmail(String toEmail, String userName, String verificationToken) {
        log.info("Iniciando envio de e-mail de verificação para {}", toEmail);
        try {
            String verificationUrl = frontendUrl + "/confirmacao-email?token=" + verificationToken;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Suporte iCar");
            helper.setTo(toEmail);
            helper.setSubject("Confirme seu e-mail - iCar");

            String htmlContent = buildVerificationEmailHtml(userName, verificationUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("E-mail de verificação enviado com sucesso para {}", toEmail);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de verificação para {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String toEmail, String userName, String resetLink) {
        log.info("Iniciando envio de e-mail de redefinição de senha para {}", toEmail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Suporte iCar");
            helper.setTo(toEmail);
            helper.setSubject("Redefinição de Senha - iCar");

            String htmlContent = buildPasswordResetHtml(userName, resetLink);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("E-mail de redefinição de senha enviado com sucesso para {}", toEmail);
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de redefinição de senha para {}: {}", toEmail, e.getMessage());
        }
    }

    private String getBaseEmailStyle() {
        String icarBlue = "#0d5188";
        String icarBlueHover = "#0a3b64";
        String lightGrayBg = "#f8f9fa";
        String textColor = "#343a40";
        String lightTextColor = "#6c757d";

        return "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; color: " + textColor + "; width: 100% !important; margin: 0 !important; padding: 0 !important; background-color: " + lightGrayBg + "; }" +
                ".container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); overflow: hidden; border: 1px solid #dee2e6; }" +
                ".header { padding: 30px; text-align: center; background-color: #ffffff; border-bottom: 1px solid #dee2e6; }" +
                ".header img { max-width: 140px; }" +
                ".content { padding: 30px 40px; }" +
                "h2 { color: " + icarBlue + "; margin-top: 0; font-size: 24px; font-weight: 600; }" +
                "p { margin-bottom: 20px; font-size: 16px; }" +
                ".button-container { text-align: center; margin: 30px 0; }" +
                ".button { display: inline-block; padding: 15px 30px; background-color: " + icarBlue + "; color: #ffffff !important; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px; transition: background-color 0.3s; }" +
                ".button:hover { background-color: " + icarBlueHover + "; }" +
                ".fallback-link { font-size: 14px; color: " + lightTextColor + "; text-align: center; margin-top: 20px; }" +
                ".fallback-link a { color: " + icarBlue + "; text-decoration: underline; }" +
                "hr { border: 0; border-top: 1px solid #dee2e6; margin: 30px 0; }" +
                ".footer { padding: 30px; text-align: center; font-size: 12px; color: " + lightTextColor + "; background-color: #f8f9fa; }";
    }

    private String buildVerificationEmailHtml(String userName, String verificationUrl) {
        return "<!DOCTYPE html><html lang=\"pt-BR\"><head><meta charset=\"UTF-8\"><title>Confirmação de E-mail - iCar</title><style>" + getBaseEmailStyle() + "</style></head>" +
                "<body><div class=\"container\">" +
                "<div class=\"header\"><a href=\"https://icarplus.com.br\" target=\"_blank\"><img src=\"" + logoUrl + "\" alt=\"iCar Logo\"></a></div>" +
                "<div class=\"content\">" +
                "<h2>Confirme seu endereço de e-mail</h2>" +
                "<p>Olá " + userName + ",</p>" +
                "<p>Estamos quase lá! Para garantir a segurança da sua conta, por favor, confirme seu endereço de e-mail clicando no botão abaixo.</p>" +
                "<div class=\"button-container\"><a href=\"" + verificationUrl + "\" class=\"button\">CONFIRMAR MEU E-MAIL</a></div>" +
                "<p class=\"fallback-link\">Se o botão não funcionar, <a href=\"" + verificationUrl + "\">clique aqui</a>.</p>" +
                "<hr>" +
                "<p>Atenciosamente,<br><strong>Equipe iCar</strong></p>" +
                "</div>" +
                "<div class=\"footer\"><p>&copy; " + LocalDate.now().getYear() + " iCar. Todos os direitos reservados.</p></div>" +
                "</div></body></html>";
    }

    private String buildPasswordResetHtml(String userName, String resetLink) {
        return "<!DOCTYPE html><html lang=\"pt-BR\"><head><meta charset=\"UTF-8\"><title>Redefinição de Senha - iCar</title><style>" + getBaseEmailStyle() + "</style></head>" +
                "<body><div class=\"container\">" +
                "<div class=\"header\"><a href=\"https://www.icarplus.com.br\" target=\"_blank\"><img src=\"" + logoUrl + "\" alt=\"iCar Logo\"></a></div>" +
                "<div class=\"content\">" +
                "<h2>Redefinição de Senha</h2>" +
                "<p>Olá " + userName + ",</p>" +
                "<p>Recebemos uma solicitação para redefinir a senha da sua conta iCar. Para criar uma nova senha, clique no botão abaixo. Este link expirará em 60 minutos.</p>" +
                "<div class=\"button-container\"><a href=\"" + resetLink + "\" class=\"button\">REDEFINIR MINHA SENHA</a></div>" +
                "<p class=\"fallback-link\">Se o botão não funcionar, <a href=\"" + resetLink + "\">clique aqui</a>.</p>" +
                "<hr>" +
                "<p style=\"font-size: 14px; color: #6c757d;\">Se você não solicitou esta alteração, pode ignorar este e-mail com segurança.</p>" +
                "<p>Atenciosamente,<br><strong>Equipe iCar</strong></p>" +
                "</div>" +
                "<div class=\"footer\"><p>&copy; " + LocalDate.now().getYear() + " iCar. Todos os direitos reservados.</p></div>" +
                "</div></body></html>";
    }
}