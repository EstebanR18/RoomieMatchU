package co.edu.ucentral.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "fotos_residencia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FotoResidenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Url pública o path en storage
    @Column(nullable = false)
    private String url;

    // Nombre original del archivo (opcional)
    private String filename;

    // Relation to PerfilTengoLugar (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_tengo_id")
    private PerfilTengoLugarEntity perfilTengo;

    // Relation to PerfilBuscoLugar (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_busco_id")
    private PerfilBuscoLugarEntity perfilBusco;
}
