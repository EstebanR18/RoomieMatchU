package co.edu.ucentral.service;

import co.edu.ucentral.dto.PerfilBuscoLugarRequestDTO;
import co.edu.ucentral.dto.PerfilTengoLugarRequestDTO;
import co.edu.ucentral.entity.PerfilBuscoLugarEntity;
import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.repository.PerfilBuscoLugarRepository;
import co.edu.ucentral.repository.PerfilTengoLugarRepository;
import co.edu.ucentral.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

@ApplicationScoped
public class PerfilService {

    @Inject
    UserRepository userRepository;

    @Inject
    PerfilBuscoLugarRepository perfilBuscoRepo;

    @Inject
    PerfilTengoLugarRepository perfilTengoRepo;

    // Validación común de edad
    private void validarEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.plusYears(18).isAfter(LocalDate.now())) {
            throw new RuntimeException("Debe ser mayor de 18 años");
        }
    }

    // Crear perfil "Busco un lugar"
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

    // Crear perfil "Tengo un lugar"
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

    // Editar perfil "Busco un lugar"
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
        existente.setGenero(nuevosDatos.getGenero());
        existente.setFuma(nuevosDatos.getFuma());
        existente.setAlergico(nuevosDatos.getAlergico());
        existente.setDetalleAlergia(nuevosDatos.getDetalleAlergia());
        existente.setIdioma(nuevosDatos.getIdioma());
        existente.setTelefono(nuevosDatos.getTelefono());
        existente.setDescripcionLibre(nuevosDatos.getDescripcionLibre());
        existente.setMascota(nuevosDatos.getMascota());
    }

    // Editar perfil "Tengo un lugar"
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

    // Cambiar tipo de perfil
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
}

