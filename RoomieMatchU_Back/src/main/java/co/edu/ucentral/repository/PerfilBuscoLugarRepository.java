package co.edu.ucentral.repository;

import co.edu.ucentral.entity.PerfilBuscoLugarEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PerfilBuscoLugarRepository implements PanacheRepository<PerfilBuscoLugarEntity> {

    public PerfilBuscoLugarEntity findByUserId(Long userId) {
        return find("user.id", userId).firstResult();
    }

    public List<PerfilBuscoLugarEntity> findAllExceptUser(Long excludeUserId) {
        return find("user.id != ?1", excludeUserId).list();
    }
}
