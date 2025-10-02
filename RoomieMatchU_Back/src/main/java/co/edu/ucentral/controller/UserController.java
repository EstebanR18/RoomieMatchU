package co.edu.ucentral.controller;

import co.edu.ucentral.entity.UserEntity;
import co.edu.ucentral.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public Response register(UserDTO dto) {
        try {
            UserEntity user = UserEntity.builder()
                    .nombreCompleto(dto.nombreCompleto)
                    .correo(dto.correo)
                    .telefono(dto.telefono)
                    .usuario(dto.usuario)
                    .build();

            userService.registerUser(user, dto.password);
            return Response.ok("Registro exitoso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginDTO dto) {
        try {
            UserEntity user = userService.login(dto.correo, dto.password);
            return Response.ok(
                    java.util.Map.of(
                            "mensaje", "Bienvenido " + user.getUsuario(),
                            "userId", user.getId()
                    )
            ).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    // DTOs internos
    public static class UserDTO {
        public String nombreCompleto;
        public String correo;
        public String telefono;
        public String usuario;
        public String password;
    }

    public static class LoginDTO {
        public String correo;
        public String password;
    }
}