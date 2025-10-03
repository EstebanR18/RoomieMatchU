package co.edu.ucentral.repository;

import co.edu.ucentral.entity.PasswordResetTokenEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

@ApplicationScoped
public class PasswordResetTokenRepository implements PanacheRepository<PasswordResetTokenEntity> {

    public PasswordResetTokenEntity findValidToken(String correo, String token) {
        return find("correo = ?1 and token = ?2 and expiration > ?3", correo, token, LocalDateTime.now())
                .firstResult();
    }

    public void deleteExpiredTokens() {
        delete("expiration < ?1", LocalDateTime.now());
    }

}