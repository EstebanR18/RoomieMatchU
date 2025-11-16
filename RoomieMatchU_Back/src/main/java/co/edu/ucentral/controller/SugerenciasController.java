package co.edu.ucentral.controller;

import co.edu.ucentral.dto.PerfilResponseDTO;
import co.edu.ucentral.service.SugerenciasService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/sugerencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SugerenciasController {

    @Inject
    SugerenciasService service;

    @GET
    @Path("/{userId}")
    public List<PerfilResponseDTO> listar(@PathParam("userId") Long userId) {
        return service.obtenerSugerencias(userId);
    }
}

