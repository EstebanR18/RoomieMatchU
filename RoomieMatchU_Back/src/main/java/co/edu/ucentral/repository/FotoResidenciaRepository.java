package co.edu.ucentral.repository;

import jakarta.enterprise.context.ApplicationScoped;
import co.edu.ucentral.entity.FotoResidenciaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class FotoResidenciaRepository implements PanacheRepository<FotoResidenciaEntity> {
    // Métodos personalizados si son necesarios
}