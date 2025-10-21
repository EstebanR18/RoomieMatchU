package co.edu.ucentral.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public UserEntity registerUser(UserEntity user, String password) {

        // Validar campos vacíos
        if (user.getNombreCompleto() == null || user.getNombreCompleto().isBlank()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }
        if (user.getUsuario() == null || user.getUsuario().isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (user.getTelefono() == null || user.getTelefono().isBlank()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }
        if (user.getCorreo() == null || user.getCorreo().isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        // Validar duplicados
        if (userRepository.existsByCorreo(user.getCorreo())) {
            throw new RuntimeException("Correo ya registrado");
        }
        if (userRepository.existsByUsuario(user.getUsuario())) {
            throw new RuntimeException("Usuario ya registrado");
        }

        // Validar formato de correo
        if (!user.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Formato de correo inválido");
        }

        // Validar seguridad de contraseña
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new RuntimeException(
                    "La contraseña debe tener mínimo 8 caracteres, incluir mayúscula, minúscula, número y símbolo"
            );
        }

        // Hashear contraseña
        String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        user.setPasswordHash(hash);

        // Guardar
        userRepository.persist(user);
        return user;
    }


    public UserEntity login(String correo, String password) {
        UserEntity user = userRepository.findByCorreo(correo);
        if (user == null) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash());
        if (!result.verified) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        return user;
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id);
    }

}