package co.edu.ucentral.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PerfilResponseDTO {
    public Long id;
    public String tipo;
    public String fotoPerfil;
    public LocalDate fechaNacimiento;
    public String barrio;
    public String habitos;
    public String genero;
    public Boolean fuma;
    public Boolean alergico;
    public String detalleAlergia;
    public String idioma;
    public String telefono;
    public String descripcionLibre;
    public Boolean mascota;

    // BUSCO
    public BigDecimal presupuesto;
    public String tipoHabitacion;
    public String tiempoEstancia;
    public Integer personasConvivencia;
    public LocalDate fechaMudanza;
    public String serviciosDeseados;
    public List<String> fotosResidenciaUrls;

    // TENGO
    public BigDecimal arriendo;
    public Integer cantidadHabitaciones;
    public Integer maxRoomies;
    public String reglasConvivencia;
    public String serviciosIncluidos;
}



