package co.edu.ucentral.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "perfil_tengo_lugar")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PerfilTengoLugarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    // Obligatorios
    @Column(nullable = false)
    private String fotoPerfil;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private BigDecimal arriendo;

    @Column(nullable = false)
    private Integer cantidadHabitaciones;

    @Column(nullable = false)
    private Integer maxRoomies;

    @Column(nullable = false)
    private String barrio;

    @Column(nullable = false)
    private String habitos;

    // Opcionales
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
}
