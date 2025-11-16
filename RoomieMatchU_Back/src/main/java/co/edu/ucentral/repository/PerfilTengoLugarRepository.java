package co.edu.ucentral.repository;

import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PerfilTengoLugarRepository implements PanacheRepository<PerfilTengoLugarEntity> {

    public PerfilTengoLugarEntity findByUserId(Long userId) {
        return find("user.id", userId).firstResult();
    }

    public List<PerfilTengoLugarEntity> findAllExceptUser(Long excludeUserId) {
        return find("user.id != ?1", excludeUserId).list();
    }
}
