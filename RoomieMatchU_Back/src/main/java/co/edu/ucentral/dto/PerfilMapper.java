package co.edu.ucentral.dto;

import co.edu.ucentral.entity.FotoResidenciaEntity;
import co.edu.ucentral.entity.PerfilBuscoLugarEntity;
import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.stream.Collectors;

@ApplicationScoped
public class PerfilMapper {

    public PerfilResponseDTO toResponse(PerfilBuscoLugarEntity e) {
        PerfilResponseDTO dto = new PerfilResponseDTO();

        dto.id = e.getId();
        dto.userId = e.getUser().getId();
        dto.usuario = e.getUser().getUsuario();
        dto.tipo = "BUSCO_LUGAR";
        dto.fotoPerfil = e.getFotoPerfil();
        dto.fechaNacimiento = e.getFechaNacimiento();
        dto.barrio = e.getBarrio();
        dto.habitos = e.getHabitos();
        dto.genero = e.getGenero();
        dto.fuma = e.getFuma();
        dto.alergico = e.getAlergico();
        dto.detalleAlergia = e.getDetalleAlergia();
        dto.idioma = e.getIdioma();
        dto.telefono = e.getTelefono();
        dto.descripcionLibre = e.getDescripcionLibre();
        dto.mascota = e.getMascota();

        // Campos BUSCO
        dto.presupuesto = e.getPresupuesto();
        dto.tipoHabitacion = e.getTipoHabitacion();
        dto.tiempoEstancia = e.getTiempoEstancia();
        dto.personasConvivencia = e.getPersonasConvivencia();
        dto.fechaMudanza = e.getFechaMudanza();
        dto.serviciosDeseados = e.getServiciosDeseados();

        // Fotos
        dto.fotosResidenciaUrls = e.getFotosResidencia()
                .stream()
                .map(FotoResidenciaEntity::getUrl)
                .collect(Collectors.toList());

        return dto;
    }

    public PerfilResponseDTO toResponse(PerfilTengoLugarEntity e) {
        PerfilResponseDTO dto = new PerfilResponseDTO();

        dto.id = e.getId();
        dto.userId = e.getUser().getId();
        dto.usuario = e.getUser().getUsuario();
        dto.tipo = "TENGO_LUGAR";
        dto.fotoPerfil = e.getFotoPerfil();
        dto.fechaNacimiento = e.getFechaNacimiento();
        dto.barrio = e.getBarrio();
        dto.habitos = e.getHabitos();
        dto.genero = e.getGenero();
        dto.fuma = e.getFuma();
        dto.alergico = e.getAlergico();
        dto.detalleAlergia = e.getDetalleAlergia();
        dto.idioma = e.getIdioma();
        dto.telefono = e.getTelefono();
        dto.descripcionLibre = e.getDescripcionLibre();
        dto.mascota = e.getMascota();

        // Campos TENGO
        dto.arriendo = e.getArriendo();
        dto.cantidadHabitaciones = e.getCantidadHabitaciones();
        dto.maxRoomies = e.getMaxRoomies();
        dto.reglasConvivencia = e.getReglasConvivencia();
        dto.serviciosIncluidos = e.getServiciosIncluidos();

        // Fotos
        dto.fotosResidenciaUrls = e.getFotosResidencia()
                .stream()
                .map(FotoResidenciaEntity::getUrl)
                .collect(Collectors.toList());

        return dto;
    }
}
