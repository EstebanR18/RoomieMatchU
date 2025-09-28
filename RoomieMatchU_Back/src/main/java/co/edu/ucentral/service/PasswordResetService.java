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

    // Paso 1: solicitar código
    @Transactional
    public String requestPasswordReset(String correo) {
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

        // TODO: enviar por correo, por ahora devolver en respuesta
        return token;
    }

    // Paso 2: cambiar contraseña
    @Transactional
    public void resetPassword(String correo, String token, String newPassword) {
        PasswordResetTokenEntity resetToken = tokenRepository.findValidToken(correo, token);

        if (resetToken == null) {
            throw new RuntimeException("Código incorrecto o vencido");
        }

        if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new RuntimeException("La contraseña no cumple requisitos de seguridad");
        }

        UserEntity user = userRepository.findByCorreo(correo);
        if (user == null) {
            throw new RuntimeException("Correo no registrado");
        }

        // Hashear nueva contraseña
        String hash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
        user.setPasswordHash(hash);

        // Actualizar usuario
        userRepository.persist(user);

        // Invalida el token (lo borramos)
        tokenRepository.delete(resetToken);
    }
}