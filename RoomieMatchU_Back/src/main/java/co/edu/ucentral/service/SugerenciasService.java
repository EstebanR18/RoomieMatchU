package co.edu.ucentral.service;

import co.edu.ucentral.dto.PerfilMapper;
import co.edu.ucentral.dto.PerfilResponseDTO;
import co.edu.ucentral.entity.PerfilBuscoLugarEntity;
import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import co.edu.ucentral.repository.PerfilBuscoLugarRepository;
import co.edu.ucentral.repository.PerfilTengoLugarRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SugerenciasService {

    @Inject
    PerfilBuscoLugarRepository buscoRepo;

    @Inject
    PerfilTengoLugarRepository tengoRepo;

    @Inject
    PerfilMapper mapper;

    public List<PerfilResponseDTO> obtenerSugerencias(Long userId) {

        PerfilBuscoLugarEntity busco = buscoRepo.findByUserId(userId);
        PerfilTengoLugarEntity tengo = tengoRepo.findByUserId(userId);

        if (busco != null) {
            // Usuario BUSCO → mostrar TENGO
            return tengoRepo.findAllExceptUser(userId)
                    .stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        }

        if (tengo != null) {
            // Usuario TENGO → mostrar BUSCO
            return buscoRepo.findAllExceptUser(userId)
                    .stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("El usuario no tiene perfil creado.");
    }
}
