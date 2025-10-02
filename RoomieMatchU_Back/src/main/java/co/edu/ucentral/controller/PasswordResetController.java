package co.edu.ucentral.controller;

import co.edu.ucentral.service.PasswordResetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/password")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PasswordResetController {

    @Inject
    PasswordResetService resetService;

    @POST
    @Path("/request")
    public Response request(ResetRequestDTO dto) {
        try {
            resetService.requestPasswordReset(dto.correo);

            return Response.ok(
                    java.util.Map.of(
                            "mensaje", "Si el correo está registrado, se ha enviado un código de verificación"
                    )
            ).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/validate")
    public Response validateToken(ValidateTokenDTO dto) {
        try {
            boolean isValid = resetService.validateToken(dto.correo, dto.token);

            if (isValid) {
                return Response.ok(
                        java.util.Map.of(
                                "mensaje", "Código válido"
                        )
                ).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Código incorrecto o vencido")
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }



    @POST
    @Path("/reset")
    public Response reset(ResetPasswordDTO dto) {
        try {
            resetService.resetPassword(
                    dto.correo,
                    dto.token,
                    dto.newPassword,
                    dto.confirmPassword
            );
            return Response.ok(
                    java.util.Map.of(
                            "mensaje", "Contraseña actualizada con éxito"
                    )
            ).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }


    // DTOs
    public static class ResetRequestDTO {
        public String correo;
    }

    public static class ValidateTokenDTO {
        public String correo;
        public String token;
    }

    public static class ResetPasswordDTO {
        public String correo;
        public String token;
        public String newPassword;
        public String confirmPassword;
    }
}
