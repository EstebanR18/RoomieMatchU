package co.edu.ucentral.controller;

import co.edu.ucentral.dto.*;
import co.edu.ucentral.dto.multipart.FotoPerfilForm;
import co.edu.ucentral.dto.multipart.FotosResidenciaForm;
import co.edu.ucentral.entity.FotoResidenciaEntity;
import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.repository.PerfilBuscoLugarRepository;
import co.edu.ucentral.repository.PerfilTengoLugarRepository;
import co.edu.ucentral.repository.UserRepository;
import co.edu.ucentral.service.PerfilService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import java.util.List;

@Path("/api/perfil")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PerfilController {

    @Inject
    PerfilService perfilService;

    @Inject
    PerfilBuscoLugarRepository perfilBuscoRepo;

    @Inject
    PerfilTengoLugarRepository perfilTengoRepo;

    @Inject
    UserRepository userRepository;

    // Crear perfil "Busco un lugar"
    @POST
    @Path("/busco-lugar/{userId}")
    public Response crearPerfilBusco(@PathParam("userId") Long userId, PerfilBuscoLugarRequestDTO data) {
        try {
            perfilService.crearPerfilBusco(userId, data);
            return Response.ok(new MensajeResponseDTO("Perfil 'Busco un lugar' creado correctamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    // Crear perfil "Tengo un lugar"
    @POST
    @Path("/tengo-lugar/{userId}")
    public Response crearPerfilTengo(@PathParam("userId") Long userId, PerfilTengoLugarRequestDTO data) {
        try {
            perfilService.crearPerfilTengo(userId, data);
            return Response.ok(new MensajeResponseDTO("Perfil 'Tengo un lugar' creado correctamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    // Editar perfil "Busco un lugar"
    @PUT
    @Path("/busco-lugar/{userId}")
    public Response editarPerfilBusco(@PathParam("userId") Long userId, PerfilBuscoLugarRequestDTO data) {
        try {
            perfilService.editarPerfilBusco(userId, data);
            return Response.ok(new MensajeResponseDTO("Perfil 'Busco un lugar' actualizado correctamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    // Editar perfil "Tengo un lugar"
    @PUT
    @Path("/tengo-lugar/{userId}")
    public Response editarPerfilTengo(@PathParam("userId") Long userId, PerfilTengoLugarRequestDTO data) {
        try {
            perfilService.editarPerfilTengo(userId, data);
            return Response.ok(new MensajeResponseDTO("Perfil 'Tengo un lugar' actualizado correctamente")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    // Cambiar tipo de perfil
    @PUT
    @Path("/cambiar-tipo/{userId}")
    public Response cambiarTipoPerfil(@PathParam("userId") Long userId, CambioTipoPerfilDTO dto) {
        try {
            perfilService.cambiarTipoPerfil(userId, dto.getNuevoTipo());
            return Response.ok(new MensajeResponseDTO("Tipo de perfil actualizado a " + dto.getNuevoTipo())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    // Obtener perfil de usuario
    @GET
    @Path("/{userId}")
    public Response obtenerPerfil(@PathParam("userId") Long userId) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MensajeResponseDTO("Usuario no encontrado"))
                    .build();
        }

        if (user.getPerfilTipo() == UserEntity.PerfilTipo.BUSCO_LUGAR) {
            var perfil = perfilBuscoRepo.findByUserId(userId);
            if (perfil == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new MensajeResponseDTO("Perfil no encontrado")).build();
            }

            PerfilResponseDTO dto = new PerfilResponseDTO();
            dto.id = perfil.getId();
            dto.tipo = "BUSCO_LUGAR";
            dto.fotoPerfil = perfil.getFotoPerfil();
            dto.fechaNacimiento = perfil.getFechaNacimiento();
            dto.barrio = perfil.getBarrio();
            dto.habitos = perfil.getHabitos();
            dto.genero = perfil.getGenero();
            dto.fuma = perfil.getFuma();
            dto.alergico = perfil.getAlergico();
            dto.detalleAlergia = perfil.getDetalleAlergia();
            dto.idioma = perfil.getIdioma();
            dto.telefono = perfil.getTelefono();
            dto.descripcionLibre = perfil.getDescripcionLibre();
            dto.mascota = perfil.getMascota();
            dto.presupuesto = perfil.getPresupuesto();

            // nuevos campos
            dto.tipoHabitacion = perfil.getTipoHabitacion();
            dto.tiempoEstancia = perfil.getTiempoEstancia();
            dto.personasConvivencia = perfil.getPersonasConvivencia();
            dto.fechaMudanza = perfil.getFechaMudanza();
            dto.serviciosDeseados = perfil.getServiciosDeseados();

            // fotos
            if (perfil.getFotosResidencia() != null && !perfil.getFotosResidencia().isEmpty()) {
                dto.fotosResidenciaUrls = perfil.getFotosResidencia().stream().map(FotoResidenciaEntity::getUrl).toList();
            }

            return Response.ok(dto).build();
        }

        if (user.getPerfilTipo() == UserEntity.PerfilTipo.TENGO_LUGAR) {
            var perfil = perfilTengoRepo.findByUserId(userId);
            if (perfil == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new MensajeResponseDTO("Perfil no encontrado")).build();
            }

            PerfilResponseDTO dto = new PerfilResponseDTO();
            dto.id = perfil.getId();
            dto.tipo = "TENGO_LUGAR";
            dto.fotoPerfil = perfil.getFotoPerfil();
            dto.fechaNacimiento = perfil.getFechaNacimiento();
            dto.barrio = perfil.getBarrio();
            dto.habitos = perfil.getHabitos();
            dto.genero = perfil.getGenero();
            dto.fuma = perfil.getFuma();
            dto.alergico = perfil.getAlergico();
            dto.detalleAlergia = perfil.getDetalleAlergia();
            dto.idioma = perfil.getIdioma();
            dto.telefono = perfil.getTelefono();
            dto.descripcionLibre = perfil.getDescripcionLibre();
            dto.mascota = perfil.getMascota();

            // Campos exclusivos
            dto.arriendo = perfil.getArriendo();
            dto.cantidadHabitaciones = perfil.getCantidadHabitaciones();
            dto.maxRoomies = perfil.getMaxRoomies();
            dto.reglasConvivencia = perfil.getReglasConvivencia();
            dto.serviciosIncluidos = perfil.getServiciosIncluidos();

            if (perfil.getFotosResidencia() != null && !perfil.getFotosResidencia().isEmpty()) {
                dto.fotosResidenciaUrls = perfil.getFotosResidencia().stream().map(FotoResidenciaEntity::getUrl).toList();
            }

            return Response.ok(dto).build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new MensajeResponseDTO("El usuario no tiene perfil asignado")).build();
    }

    // ----------------- MULTIPART endpoints -----------------

    // Subir foto de perfil
    @POST
    @Path("/{userId}/foto-perfil")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirFotoPerfil(
            @PathParam("userId") Long userId,
            @MultipartForm FotoPerfilForm form
    ) {
        try {
            String url = perfilService.subirFotoPerfil(userId, form.file);
            return Response.ok(url).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    //Fotos Residencia
    @POST
    @Path("/{userId}/fotos-residencia")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirFotosResidencia(
            @PathParam("userId") Long userId,
            @MultipartForm FotosResidenciaForm form
    ) {
        try {
            List<String> urls = perfilService.subirFotosResidencia(userId, form.files);
            return Response.ok(urls).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

}


