package co.edu.ucentral.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PerfilResponseDTO {
    public Long id;
    public String tipo; // BUSCO_LUGAR o TENGO_LUGAR

    public String fotoPerfil;
    public LocalDate fechaNacimiento; // <-- usa este tipo si tu entidad lo maneja así
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

    // Solo para "Busco un lugar"
    public BigDecimal presupuesto;

    // Solo para "Tengo un lugar"
    public BigDecimal arriendo;
    public Integer cantidadHabitaciones;
    public Integer maxRoomies;
    public String reglasConvivencia;
    public String serviciosIncluidos;
}



