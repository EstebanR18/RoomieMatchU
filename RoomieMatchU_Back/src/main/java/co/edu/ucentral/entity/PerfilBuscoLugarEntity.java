package co.edu.ucentral.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "perfil_busco_lugar")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PerfilBuscoLugarEntity {

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
    private BigDecimal presupuesto;

    @Column(nullable = false)
    private String barrio;

    @Column(nullable = false)
    private String habitos; // horarios, estilo de vida, etc.

    // Opcionales
    private String genero; // Masculino, Femenino, Otro
    private Boolean fuma;
    private Boolean alergico;
    private String detalleAlergia;
    private String idioma;
    private String telefono;
    private String descripcionLibre;
    private Boolean mascota;
}
