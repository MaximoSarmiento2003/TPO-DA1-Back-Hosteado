package com.example.tpoDA.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    // ─── Activación de cuenta (post-aprobación) ───────────────────────────────

    public void sendSetPasswordEmail(String toEmail, String token) {
        String link = frontendUrl + "/set-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("AuctionPro — ¡Tu cuenta fue aprobada!");
        message.setText(
            "¡Hola!\n\n" +
            "Buenas noticias: tu solicitud de registro en AuctionPro fue aprobada.\n\n" +
            "Hacé clic en el siguiente link para crear tu contraseña y activar tu cuenta:\n\n" +
            link + "\n\n" +
            "Este link expira en 48 horas.\n\n" +
            "Si no solicitaste este registro, ignorá este correo.\n\n" +
            "— El equipo de AuctionPro"
        );
        mailSender.send(message);
    }

    // ─── Recuperación de contraseña ───────────────────────────────────────────

    public void sendPasswordResetEmail(String toEmail, String token) {
        String link = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("AuctionPro — Recuperación de contraseña");
        message.setText(
            "Hola,\n\n" +
            "Recibimos una solicitud para restablecer la contraseña de tu cuenta en AuctionPro.\n\n" +
            "Hacé clic en el siguiente link para crear una nueva contraseña:\n\n" +
            link + "\n\n" +
            "Este link expira en 1 hora.\n\n" +
            "Si no solicitaste este cambio, podés ignorar este correo. Tu contraseña actual no será modificada.\n\n" +
            "— El equipo de AuctionPro"
        );
        mailSender.send(message);
    }

    // ─── Rechazo de registro ──────────────────────────────────────────────────

    public void sendRejectionEmail(String toEmail, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("AuctionPro — Solicitud de registro no aprobada");
        message.setText(
            "Hola,\n\n" +
            "Lamentablemente, tu solicitud de registro en AuctionPro no pudo ser aprobada " +
            "en este momento.\n\n" +
            (reason != null && !reason.isBlank()
                ? "Motivo: " + reason + "\n\n"
                : "") +
            "Si creés que esto es un error o querés más información, " +
            "comunicate con nuestro equipo de soporte.\n\n" +
            "— El equipo de AuctionPro"
        );
        mailSender.send(message);
    }

    // ─── Mail de acceso al streaming ─────────────────────────────────────────

    public void sendStreamingEmail(String toEmail, String auctionName, String streamingLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("AuctionPro — ¡Tu subasta está a punto de comenzar!");
        message.setText(
            "Hola,\n\n" +
            "La subasta \"" + (auctionName != null ? auctionName : "") + "\" está activa.\n\n" +
            "Accedé al streaming en vivo desde este link:\n\n" +
            streamingLink + "\n\n" +
            "¡Buena suerte con tus ofertas!\n\n" +
            "— El equipo de AuctionPro"
        );
        mailSender.send(message);
    }
}
