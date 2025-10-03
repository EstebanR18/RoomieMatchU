package co.edu.ucentral.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import co.edu.ucentral.entity.PasswordResetTokenEntity;
import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.repository.PasswordResetTokenRepository;
import co.edu.ucentral.repository.UserRepository;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class PasswordResetService {

    private static final Logger LOG = Logger.getLogger(PasswordResetService.class);

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordResetTokenRepository tokenRepository;

    @Inject
    Mailer mailer;

    @Transactional
    public void requestPasswordReset(String correo) {
        LOG.info("Intentando generar token para correo: " + correo);

        UserEntity user = userRepository.findByCorreo(correo);
        if (user == null) {
            LOG.warn("Correo no registrado: " + correo);
            throw new RuntimeException("Correo no registrado");
        }

        String token = String.format("%06d", new java.util.Random().nextInt(999999));
        LOG.info("Token generado: " + token);

        PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
                .correo(correo)
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.persist(resetToken);
        LOG.info("Token guardado en base de datos");

        try {
            mailer.send(
                    Mail.withText(
                            correo,
                            "Código para restablecer contraseña",
                            "Tu código es: " + token + "\nVálido por 15 minutos."
                    )
            );
            LOG.info("Correo enviado a: " + correo);
        } catch (Exception e) {
            LOG.error("Error enviando correo: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el correo");
        }
    }



    public boolean validateToken(String correo, String token) {
        PasswordResetTokenEntity resetToken =
                tokenRepository.findValidToken(correo, token);

        if (resetToken == null) {
            return false;
        }
        return true;
    }


    @Transactional
    public void resetPassword(String correo, String token, String newPassword, String confirmPassword) {

        // Validar token
        PasswordResetTokenEntity resetToken = tokenRepository.findValidToken(correo, token);
        if (resetToken == null) {
            throw new RuntimeException("Código incorrecto o vencido");
        }

        // Validar requisitos de contraseña
        if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new RuntimeException("La contraseña no cumple requisitos de seguridad");
        }

        UserEntity user = userRepository.findByCorreo(correo);
        if (user == null) {
            throw new RuntimeException("Correo no registrado");
        }

        // Hashear y guardar
        String hash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
        user.setPasswordHash(hash);
        userRepository.persist(user);

        // Eliminar token usado
        tokenRepository.delete(resetToken);
    }

}