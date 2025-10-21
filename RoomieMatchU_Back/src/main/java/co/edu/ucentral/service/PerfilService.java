package co.edu.ucentral.service;

import co.edu.ucentral.dto.PerfilBuscoLugarRequestDTO;
import co.edu.ucentral.dto.PerfilTengoLugarRequestDTO;
import co.edu.ucentral.entity.FotoResidenciaEntity;
import co.edu.ucentral.entity.PerfilBuscoLugarEntity;
import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.repository.FotoResidenciaRepository;
import co.edu.ucentral.repository.PerfilBuscoLugarRepository;
import co.edu.ucentral.repository.PerfilTengoLugarRepository;
import co.edu.ucentral.repository.UserRepository;
import co.edu.ucentral.storage.S3Uploader;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@ApplicationScoped
public class PerfilService {

    @Inject UserRepository userRepository;
    @Inject PerfilBuscoLugarRepository perfilBuscoRepo;
    @Inject PerfilTengoLugarRepository perfilTengoRepo;
    @Inject FotoResidenciaRepository fotoRepo;
    @Inject S3Uploader s3Uploader;


    // ------------------ VALIDACIÓN ------------------
    private void validarEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.plusYears(18).isAfter(LocalDate.now())) {
            throw new RuntimeException("Debe ser mayor de 18 años");
        }
    }

    // ------------------ CREAR PERFIL BUSCO ------------------
    @Transactional
    public void crearPerfilBusco(Long userId, PerfilBuscoLugarRequestDTO data) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) throw new RuntimeException("Usuario no encontrado");

        validarEdad(data.getFechaNacimiento());

        PerfilBuscoLugarEntity entity = new PerfilBuscoLugarEntity();
        entity.setUser(user);
        entity.setFotoPerfil(data.getFotoPerfil());
        entity.setFechaNacimiento(data.getFechaNacimiento());
        entity.setPresupuesto(data.getPresupuesto());
        entity.setBarrio(data.getBarrio());
        entity.setHabitos(data.getHabitos());

        // Nuevos
        entity.setTipoHabitacion(data.getTipoHabitacion());
        entity.setTiempoEstancia(data.getTiempoEstancia());
        entity.setPersonasConvivencia(data.getPersonasConvivencia());
        entity.setServiciosDeseados(data.getServiciosDeseados());
        entity.setFechaMudanza(data.getFechaMudanza());

        // Opcionales
        entity.setGenero(data.getGenero());
        entity.setFuma(data.getFuma());
        entity.setAlergico(data.getAlergico());
        entity.setDetalleAlergia(data.getDetalleAlergia());
        entity.setIdioma(data.getIdioma());
        entity.setTelefono(data.getTelefono());
        entity.setDescripcionLibre(data.getDescripcionLibre());
        entity.setMascota(data.getMascota());

        perfilBuscoRepo.persist(entity);

        user.setPerfilTipo(UserEntity.PerfilTipo.BUSCO_LUGAR);
        userRepository.persist(user);
    }

    // ------------------ CREAR PERFIL TENGO ------------------
    @Transactional
    public void crearPerfilTengo(Long userId, PerfilTengoLugarRequestDTO data) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) throw new RuntimeException("Usuario no encontrado");

        validarEdad(data.getFechaNacimiento());

        PerfilTengoLugarEntity entity = new PerfilTengoLugarEntity();
        entity.setUser(user);
        entity.setFotoPerfil(data.getFotoPerfil());
        entity.setFechaNacimiento(data.getFechaNacimiento());
        entity.setArriendo(data.getArriendo());
        entity.setCantidadHabitaciones(data.getCantidadHabitaciones());
        entity.setMaxRoomies(data.getMaxRoomies());
        entity.setBarrio(data.getBarrio());
        entity.setHabitos(data.getHabitos());
        // Opcionales
        entity.setGenero(data.getGenero());
        entity.setFuma(data.getFuma());
        entity.setAlergico(data.getAlergico());
        entity.setDetalleAlergia(data.getDetalleAlergia());
        entity.setIdioma(data.getIdioma());
        entity.setTelefono(data.getTelefono());
        entity.setDescripcionLibre(data.getDescripcionLibre());
        entity.setReglasConvivencia(data.getReglasConvivencia());
        entity.setServiciosIncluidos(data.getServiciosIncluidos());
        entity.setMascota(data.getMascota());

        perfilTengoRepo.persist(entity);

        user.setPerfilTipo(UserEntity.PerfilTipo.TENGO_LUGAR);
        userRepository.persist(user);
    }

    // ------------------ EDITAR PERFIL BUSCO ------------------
    @Transactional
    public void editarPerfilBusco(Long userId, PerfilBuscoLugarRequestDTO nuevosDatos) {
        PerfilBuscoLugarEntity existente = perfilBuscoRepo.findByUserId(userId);
        if (existente == null) throw new RuntimeException("Perfil no encontrado");

        validarEdad(nuevosDatos.getFechaNacimiento());

        existente.setFotoPerfil(nuevosDatos.getFotoPerfil());
        existente.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
        existente.setPresupuesto(nuevosDatos.getPresupuesto());
        existente.setBarrio(nuevosDatos.getBarrio());
        existente.setHabitos(nuevosDatos.getHabitos());
        existente.setTipoHabitacion(nuevosDatos.getTipoHabitacion());
        existente.setTiempoEstancia(nuevosDatos.getTiempoEstancia());
        existente.setPersonasConvivencia(nuevosDatos.getPersonasConvivencia());
        existente.setFechaMudanza(nuevosDatos.getFechaMudanza());
        existente.setServiciosDeseados(nuevosDatos.getServiciosDeseados());
        existente.setGenero(nuevosDatos.getGenero());
        existente.setFuma(nuevosDatos.getFuma());
        existente.setAlergico(nuevosDatos.getAlergico());
        existente.setDetalleAlergia(nuevosDatos.getDetalleAlergia());
        existente.setIdioma(nuevosDatos.getIdioma());
        existente.setTelefono(nuevosDatos.getTelefono());
        existente.setDescripcionLibre(nuevosDatos.getDescripcionLibre());
        existente.setMascota(nuevosDatos.getMascota());
    }

    // ------------------ EDITAR PERFIL TENGO ------------------
    @Transactional
    public void editarPerfilTengo(Long userId, PerfilTengoLugarRequestDTO nuevosDatos) {
        PerfilTengoLugarEntity existente = perfilTengoRepo.findByUserId(userId);
        if (existente == null) throw new RuntimeException("Perfil no encontrado");

        validarEdad(nuevosDatos.getFechaNacimiento());

        existente.setFotoPerfil(nuevosDatos.getFotoPerfil());
        existente.setFechaNacimiento(nuevosDatos.getFechaNacimiento());
        existente.setArriendo(nuevosDatos.getArriendo());
        existente.setCantidadHabitaciones(nuevosDatos.getCantidadHabitaciones());
        existente.setMaxRoomies(nuevosDatos.getMaxRoomies());
        existente.setBarrio(nuevosDatos.getBarrio());
        existente.setHabitos(nuevosDatos.getHabitos());
        existente.setGenero(nuevosDatos.getGenero());
        existente.setFuma(nuevosDatos.getFuma());
        existente.setAlergico(nuevosDatos.getAlergico());
        existente.setDetalleAlergia(nuevosDatos.getDetalleAlergia());
        existente.setIdioma(nuevosDatos.getIdioma());
        existente.setTelefono(nuevosDatos.getTelefono());
        existente.setDescripcionLibre(nuevosDatos.getDescripcionLibre());
        existente.setReglasConvivencia(nuevosDatos.getReglasConvivencia());
        existente.setServiciosIncluidos(nuevosDatos.getServiciosIncluidos());
        existente.setMascota(nuevosDatos.getMascota());
    }

    // ------------------ CAMBIAR TIPO PERFIL ------------------
    @Transactional
    public void cambiarTipoPerfil(Long userId, UserEntity.PerfilTipo nuevoTipo) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) throw new RuntimeException("Usuario no encontrado");

        if (user.getPerfilTipo() == UserEntity.PerfilTipo.BUSCO_LUGAR) {
            PerfilBuscoLugarEntity perfil = perfilBuscoRepo.findByUserId(userId);
            if (perfil != null) perfilBuscoRepo.delete(perfil);
        } else if (user.getPerfilTipo() == UserEntity.PerfilTipo.TENGO_LUGAR) {
            PerfilTengoLugarEntity perfil = perfilTengoRepo.findByUserId(userId);
            if (perfil != null) perfilTengoRepo.delete(perfil);
        }

        user.setPerfilTipo(nuevoTipo);
        userRepository.persist(user);
    }

    // ------------------ SUBIR FOTO DE PERFIL (S3) ------------------
    @Transactional
    public String subirFotoPerfil(Long userId, FileUpload fileUpload) {
        System.out.println("📤 [BACK-SERVICE] Iniciando subida de foto de perfil...");
        System.out.println("📤 [BACK-SERVICE] User ID recibido: " + userId);

        try {
            // 1️⃣ Verificar usuario
            UserEntity user = userRepository.findById(userId);
            if (user == null) {
                System.out.println("❌ [BACK-SERVICE] Usuario no encontrado en base de datos.");
                throw new RuntimeException("Usuario no encontrado");
            }
            System.out.println("✅ [BACK-SERVICE] Usuario encontrado: " + user.getUsuario());
            System.out.println("📇 [BACK-SERVICE] Tipo de perfil: " + user.getPerfilTipo());

            // 2️⃣ Validar archivo recibido
            if (fileUpload == null) {
                System.out.println("⚠️ [BACK-SERVICE] fileUpload es nulo.");
                throw new RuntimeException("Archivo no recibido (nulo)");
            }

            if (fileUpload.uploadedFile() == null) {
                System.out.println("⚠️ [BACK-SERVICE] uploadedFile() es nulo.");
                throw new RuntimeException("No se encontró el archivo físico");
            }

            java.nio.file.Path uploadedPath = fileUpload.uploadedFile();
            System.out.println("📂 [BACK-SERVICE] Archivo recibido en ruta temporal: " + uploadedPath.toAbsolutePath());
            System.out.println("📂 [BACK-SERVICE] Nombre original: " + fileUpload.fileName());
            System.out.println("📂 [BACK-SERVICE] Tipo MIME: " + fileUpload.contentType());
            System.out.println("📂 [BACK-SERVICE] Tamaño: " + fileUpload.size() + " bytes");

            // 3️⃣ Preparar clave para S3
            String key = "perfiles/" + userId + "/" + fileUpload.fileName();
            System.out.println("🪣 [BACK-SERVICE] Clave de destino en S3: " + key);

            // 4️⃣ Subir archivo a S3
            System.out.println("☁️ [BACK-SERVICE] Iniciando subida a S3...");
            String url = s3Uploader.uploadFile(uploadedPath, key);
            System.out.println("✅ [BACK-SERVICE] Archivo subido correctamente a S3.");
            System.out.println("🌐 [BACK-SERVICE] URL generada: " + url);

            // 5️⃣ Guardar URL según el tipo de perfil
            if (user.getPerfilTipo() == UserEntity.PerfilTipo.BUSCO_LUGAR) {
                System.out.println("🧩 [BACK-SERVICE] Actualizando perfil BUSCO_LUGAR...");
                PerfilBuscoLugarEntity perfil = perfilBuscoRepo.findByUserId(userId);
                if (perfil == null) {
                    System.out.println("❌ [BACK-SERVICE] Perfil 'Busco Lugar' no encontrado para el usuario.");
                    throw new RuntimeException("Perfil 'Busco' no encontrado");
                }
                perfil.setFotoPerfil(url);
                System.out.println("✅ [BACK-SERVICE] Perfil 'Busco Lugar' actualizado con la URL.");

            } else if (user.getPerfilTipo() == UserEntity.PerfilTipo.TENGO_LUGAR) {
                System.out.println("🧩 [BACK-SERVICE] Actualizando perfil TENGO_LUGAR...");
                PerfilTengoLugarEntity perfil = perfilTengoRepo.findByUserId(userId);
                if (perfil == null) {
                    System.out.println("❌ [BACK-SERVICE] Perfil 'Tengo Lugar' no encontrado para el usuario.");
                    throw new RuntimeException("Perfil 'Tengo' no encontrado");
                }
                perfil.setFotoPerfil(url);
                System.out.println("✅ [BACK-SERVICE] Perfil 'Tengo Lugar' actualizado con la URL.");

            } else {
                System.out.println("⚠️ [BACK-SERVICE] Usuario sin tipo de perfil asignado.");
                throw new RuntimeException("Usuario no tiene tipo de perfil asignado");
            }

            System.out.println("🎉 [BACK-SERVICE] Subida de foto de perfil completada exitosamente.");
            return url;

        } catch (Exception e) {
            System.out.println("❌ [BACK-SERVICE] Error durante la subida de foto:");
            e.printStackTrace();
            throw e;
        }
    }


    // ------------------ SUBIR FOTOS DE RESIDENCIA (S3) ------------------
    @Transactional
    public List<String> subirFotosResidencia(Long userId, List<FileUpload> files) {
        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No se recibieron archivos");
        }

        UserEntity user = userRepository.findById(userId);
        if (user == null) throw new RuntimeException("Usuario no encontrado");

        PerfilBuscoLugarEntity perfilBusco = perfilBuscoRepo.findByUserId(userId);
        PerfilTengoLugarEntity perfilTengo = perfilTengoRepo.findByUserId(userId);

        if (perfilBusco == null && perfilTengo == null) {
            throw new RuntimeException("El usuario no tiene un perfil creado");
        }

        List<String> urls = new ArrayList<>();
        for (FileUpload fileUpload : files) {
            String key = "residencias/" + userId + "/" + fileUpload.fileName();
            String url = s3Uploader.uploadFile(fileUpload.uploadedFile(), key);
            FotoResidenciaEntity foto = FotoResidenciaEntity.builder()
                    .url(url)
                    .filename(fileUpload.fileName())
                    .build();

            if (perfilTengo != null) {
                foto.setPerfilTengo(perfilTengo);
                fotoRepo.persist(foto);
            } else {
                foto.setPerfilBusco(perfilBusco);
                fotoRepo.persist(foto);
            }

            urls.add(url);
        }
        return urls;
    }
}

