package co.edu.ucentral.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PerfilTengoLugarRequestDTO {

    // Obligatorios
    private String fotoPerfil;
    private LocalDate fechaNacimiento;
    private BigDecimal arriendo;
    private Integer cantidadHabitaciones;
    private Integer maxRoomies;
    private String barrio;
    private String habitos;

    // Opcionales existentes
    private String genero;
    private Boolean fuma;
    private Boolean alergico;
    private String detalleAlergia;
    private String idioma;
    private String telefono;
    private String descripcionLibre;
    private String reglasConvivencia;
    private String serviciosIncluidos;
    private Boolean mascota;

    // Nota: las fotos se añadirán mediante endpoints multipart y quedan almacenadas en la entidad FotoResidenciaEntity
}

