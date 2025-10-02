package co.edu.ucentral.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PerfilBuscoLugarRequestDTO {

    // Obligatorios
    private String fotoPerfil;
    private LocalDate fechaNacimiento;
    private BigDecimal presupuesto;
    private String barrio;
    private String habitos;

    // Opcionales
    private String genero;
    private Boolean fuma;
    private Boolean alergico;
    private String detalleAlergia;
    private String idioma;
    private String telefono;
    private String descripcionLibre;
    private Boolean mascota;
}

