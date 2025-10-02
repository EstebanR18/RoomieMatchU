package co.edu.ucentral.repository;

import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PerfilTengoLugarRepository implements PanacheRepository<PerfilTengoLugarEntity> {

    public PerfilTengoLugarEntity findByUserId(Long userId) {
        return find("user.id", userId).firstResult();
    }
}
