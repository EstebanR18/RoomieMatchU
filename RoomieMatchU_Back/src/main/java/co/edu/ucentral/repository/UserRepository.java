package co.edu.ucentral.repository;

import co.edu.ucentral.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public boolean existsByCorreo(String correo) {
        return find("correo", correo).firstResultOptional().isPresent();
    }

    public boolean existsByUsuario(String usuario) {
        return find("usuario", usuario).firstResultOptional().isPresent();
    }

    public UserEntity findByCorreo(String correo) {
        return find("correo", correo).firstResult();
    }
}