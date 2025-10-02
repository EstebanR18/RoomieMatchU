package co.edu.ucentral.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import co.edu.ucentral.entity.PasswordResetTokenEntity;
import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.repository.PasswordResetTokenRepository;
import co.edu.ucentral.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class PasswordResetService {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordResetTokenRepository tokenRepository;

    @Transactional
    public String requestPasswordReset(String correo) {
        // 1. Limpiar tokens viejos antes de crear uno nuevo
        tokenRepository.deleteExpiredTokens();

        UserEntity user = userRepository.findByCorreo(correo);
        if (user == null) {
            throw new RuntimeException("Correo no registrado");
        }

        // Generar token único
        String token = UUID.randomUUID().toString();

        PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
                .correo(correo)
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.persist(resetToken);

        return token; // más adelante lo enviaremos por correo
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

        // Validar coincidencia de contraseñas
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

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