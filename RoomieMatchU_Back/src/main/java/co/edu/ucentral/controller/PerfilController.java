package co.edu.ucentral.controller;

import co.edu.ucentral.dto.*;
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
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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

    // ---------------------- CREAR PERFIL BUSCO ----------------------
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

    // ---------------------- CREAR PERFIL TENGO ----------------------
    @POST
    @Path("/tengo-lugar/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPerfilTengo(@PathParam("userId") Long userId, PerfilTengoLugarRequestDTO data) {
        System.out.println("🚀 [PerfilController] Recibiendo solicitud de crear perfil TENGO_LUGAR para userId=" + userId);

        try {
            if (data == null) {
                System.out.println("❌ [PerfilController] El cuerpo del request es NULL");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new MensajeResponseDTO("El cuerpo del request está vacío"))
                        .build();
            }

            // --- Log de todos los campos ---
            System.out.println("📦 [PerfilController] Datos recibidos:");
            System.out.println("   fotoPerfil = " + data.getFotoPerfil());
            System.out.println("   fechaNacimiento = " + data.getFechaNacimiento());
            System.out.println("   arriendo = " + data.getArriendo());
            System.out.println("   cantidadHabitaciones = " + data.getCantidadHabitaciones());
            System.out.println("   maxRoomies = " + data.getMaxRoomies());
            System.out.println("   barrio = " + data.getBarrio());
            System.out.println("   habitos = " + data.getHabitos());
            System.out.println("   genero = " + data.getGenero());
            System.out.println("   fuma = " + data.getFuma());
            System.out.println("   alergico = " + data.getAlergico());
            System.out.println("   detalleAlergia = " + data.getDetalleAlergia());
            System.out.println("   idioma = " + data.getIdioma());
            System.out.println("   telefono = " + data.getTelefono());
            System.out.println("   descripcionLibre = " + data.getDescripcionLibre());
            System.out.println("   reglasConvivencia = " + data.getReglasConvivencia());
            System.out.println("   serviciosIncluidos = " + data.getServiciosIncluidos());
            System.out.println("   mascota = " + data.getMascota());

            // Validación básica antes de servicio
            if (data.getFechaNacimiento() == null) {
                System.out.println("⚠️ [PerfilController] fechaNacimiento es NULL");
            }
            if (data.getArriendo() == null) {
                System.out.println("⚠️ [PerfilController] arriendo es NULL");
            }

            perfilService.crearPerfilTengo(userId, data);

            System.out.println("✅ [PerfilController] Perfil TENGO_LUGAR creado correctamente para userId=" + userId);
            return Response.ok(new MensajeResponseDTO("Perfil 'Tengo un lugar' creado correctamente")).build();

        } catch (Exception e) {
            System.out.println("❌ [PerfilController] Error al crear perfil: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }

    // ---------------------- EDITAR PERFIL BUSCO ----------------------
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

    // ---------------------- EDITAR PERFIL TENGO ----------------------
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

    // ---------------------- CAMBIAR TIPO PERFIL ----------------------
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

    // ---------------------- OBTENER PERFIL ----------------------
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

    // ---------------------- MULTIPART: FOTO PERFIL ----------------------
    @POST
    @Path("/{userId}/foto-perfil")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirFotoPerfil(
            @PathParam("userId") Long userId,
            @RestForm("file") FileUpload file
    ) {
        System.out.println("📩 [BACK] Iniciando subida de foto de perfil...");
        System.out.println("📩 [BACK] User ID: " + userId);

        try {
            // --- 1️⃣ Validar archivo recibido ---
            if (file == null) {
                System.out.println("⚠️ [BACK] El archivo es nulo");
                throw new IllegalArgumentException("Archivo no recibido (nulo)");
            }

            System.out.println("📂 [BACK] Nombre del archivo: " + file.fileName());
            System.out.println("📂 [BACK] Tipo MIME: " + file.contentType());
            System.out.println("📂 [BACK] Tamaño: " + file.size());

            // --- 2️⃣ Obtener el archivo físico ---
            java.nio.file.Path tempPath = file.uploadedFile();
            if (tempPath == null || !tempPath.toFile().exists()) {
                System.out.println("⚠️ [BACK] No se encontró archivo físico en el sistema temporal");
                throw new IllegalArgumentException("Archivo no encontrado en el servidor");
            }

            System.out.println("✅ [BACK] Archivo recibido correctamente en: " + tempPath.toAbsolutePath());

            // --- 3️⃣ Llamar al servicio que sube a S3 ---
            System.out.println("☁️ [BACK] Llamando a perfilService.subirFotoPerfil...");
            String url = perfilService.subirFotoPerfil(userId, file);
            System.out.println("✅ [BACK] URL devuelta por el servicio: " + url);

            // --- 4️⃣ Responder al cliente ---
            System.out.println("🚀 [BACK] Envío exitoso al cliente");
            return Response.ok(new MensajeResponseDTO(url)).build();

        } catch (Exception e) {
            System.out.println("❌ [BACK] Error durante la subida de foto:");
            e.printStackTrace(); // 🔍 imprime stack completo en la consola del servidor

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO("Error: " + (e.getMessage() != null ? e.getMessage() : "sin mensaje")))
                    .build();
        }
    }


    // ---------------------- MULTIPART: FOTOS RESIDENCIA ----------------------
    @POST
    @Path("/{userId}/fotos-residencia")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirFotosResidencia(
            @PathParam("userId") Long userId,
            @RestForm("files") List<FileUpload> files
    ) {
        try {
            List<String> urls = perfilService.subirFotosResidencia(userId, files);
            return Response.ok(urls).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeResponseDTO(e.getMessage()))
                    .build();
        }
    }
}
