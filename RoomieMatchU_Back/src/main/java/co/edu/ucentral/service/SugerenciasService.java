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

    @Inject
    MatchScoreService scoreService;

    public List<PerfilResponseDTO> obtenerSugerencias(Long userId) {

        PerfilBuscoLugarEntity busco = buscoRepo.findByUserId(userId);
        PerfilTengoLugarEntity tengo = tengoRepo.findByUserId(userId);

        if (busco != null) {
            // Listar TODOS los TENGO y ordenar por score
            return tengoRepo.findAllExceptUser(userId)
                    .stream()
                    .sorted((a, b) -> {
                        int scoreA = scoreService.calcularScoreBuscoLugar(busco, a);
                        int scoreB = scoreService.calcularScoreBuscoLugar(busco, b);
                        return Integer.compare(scoreB, scoreA); // DESC
                    })
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        }

        if (tengo != null) {
            return buscoRepo.findAllExceptUser(userId)
                    .stream()
                    .sorted((a, b) -> {
                        int scoreA = scoreService.calcularScoreTengoLugar(tengo, a);
                        int scoreB = scoreService.calcularScoreTengoLugar(tengo, b);
                        return Integer.compare(scoreB, scoreA);
                    })
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("El usuario no tiene perfil creado.");
    }
}
